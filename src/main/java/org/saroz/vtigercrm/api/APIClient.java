package org.saroz.vtigercrm.api;

import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.saroz.vtigercrm.utilities.ConfigReader;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * <h2>APIClient</h2>
 * A unified, enterprise-grade engine that manages the entire HTTP lifecycle: 
 * from fluent request construction to real-time response validation.
 * * <p><b>Key Architecture Design:</b></p>
 * <ul>
 * <li><b>Thread Safety:</b> ThreadLocal isolation for RequestSpecification allows seamless parallel executions.</li>
 * <li><b>Unified State Loop:</b> Automatically caches the most recent response in a local thread-safe state for fluent validation chaining.</li>
 * <li><b>Traceability:</b> Implicitly documents every request attribute and assertion matrix into Log4j and ExtentReports.</li>
 * </ul>
 *
 * @author Sandeep Kumar
 */
public class APIClient {

    private final ConfigReader config;
    private final String baseURI;
    private final String email;
    private final String apiKey;

    /** Thread-local context to isolate concurrent HTTP request specifications. */
    private final ThreadLocal<RequestSpecification> requestSpecThread = new ThreadLocal<>();

    /** Thread-local storage for tracking the most recent API response to support execution-to-validation chaining. */
    private final ThreadLocal<Response> lastResponseThread = new ThreadLocal<>();

    /** Pre-calculated JsonPath optimizer for the active response thread. */
    private final ThreadLocal<JsonPath> jsonPathThread = new ThreadLocal<>();

    private final Logger log;
    private final ThreadLocal<ExtentTest> extentTest;

    /**
     * Instantiates the APIClient engine and syncs configuration keys with the active thread profile.
     */
    public APIClient(Logger log, ThreadLocal<ExtentTest> extentTest) {
        this.log = log;
        this.extentTest = extentTest;
        this.config = new ConfigReader();

        // Validating global configurations
        this.baseURI = validate(config.getBaseurl(), "Base URL");
        this.email = validate(config.getApiEmail(), "API Email");
        this.apiKey = validate(config.getApiKey(), "API Key");

        initializeRequest();
    }

    /** Sets up a clean RequestSpecification footprint for the thread context. */
    private void initializeRequest() {
        RequestSpecification spec = RestAssured.given()
                .baseUri(this.baseURI)
                .contentType(ContentType.JSON);

        if ("true".equalsIgnoreCase(System.getProperty("debug"))) {
            spec.log().all();
        }
        requestSpecThread.set(spec);
    }

    private String validate(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            String error = "[FATAL] Missing configuration value for: " + field;
            log.error(error);
            if (extentTest.get() != null) {
                extentTest.get().log(Status.FAIL, error);
            }
            throw new RuntimeException(error);
        }
        return value.trim();
    }

    // =========================================================================
    // STEP 1: AUTHENTICATION CONSTRUCTORS (Returns APIClient for chaining)
    // =========================================================================

    public APIClient useBasicAuth() {
        log.info("Configuring Preemptive Basic Authentication.");
        extentTest.get().log(Status.INFO, "Auth: Applying Preemptive Basic Authentication.");
        requestSpecThread.get().auth().preemptive().basic(email, apiKey);
        return this;
    }

    public APIClient useBearerToken(String token) {
        log.info("Attaching Bearer Token to authorization headers.");
        extentTest.get().log(Status.INFO, "Auth: Bearer Token injected into headers.");
        requestSpecThread.get().header("Authorization", "Bearer " + token);
        return this;
    }

    // =========================================================================
    // STEP 2: REQUEST META BUILDERS
    // =========================================================================

    public APIClient setHeaders(Map<String, String> headers) {
        log.info("Injecting custom header mapping: " + headers.keySet());
        requestSpecThread.get().headers(headers);
        return this;
    }

    public APIClient setQueryParams(Map<String, ?> params) {
        log.info("Configuring URL Query Parameters: " + params);
        requestSpecThread.get().queryParams(params);
        return this;
    }

    public APIClient setBody(Object body) {
        log.info("Attaching request payload serialization structural layout.");
        requestSpecThread.get().body(body);
        return this;
    }

    // =========================================================================
    // STEP 3: HTTP METHOD EXECUTIONS
    // =========================================================================

    public APIClient get(String endpoint) { return execute("GET", endpoint); }
    public APIClient post(String endpoint) { return execute("POST", endpoint); }
    public APIClient put(String endpoint) { return execute("PUT", endpoint); }
    public APIClient patch(String endpoint) { return execute("PATCH", endpoint); }
    public APIClient delete(String endpoint) { return execute("DELETE", endpoint); }

    /** Core execution router. Transports request and captures the lifecycle state. */
    private APIClient execute(String method, String endpoint) {
        log.info("Initiating [" + method + "] call targeted at endpoint: " + endpoint);
        extentTest.get().log(Status.INFO, "HTTP Trigger: [" + method + "] -> Target: " + endpoint);

        try {
            Response response = switch (method.toUpperCase()) {
                case "GET"    -> requestSpecThread.get().get(endpoint);
                case "POST"   -> requestSpecThread.get().post(endpoint);
                case "PUT"    -> requestSpecThread.get().put(endpoint);
                case "PATCH"  -> requestSpecThread.get().patch(endpoint);
                case "DELETE" -> requestSpecThread.get().delete(endpoint);
                default -> throw new IllegalArgumentException("Unsupported HTTP Method Matrix: " + method);
            };

            // Capture state variables inside ThreadLocal maps
            lastResponseThread.set(response);
            jsonPathThread.set(response.jsonPath());

            int statusCode = response.getStatusCode();
            log.info("Transaction Completed. Status Code: [" + statusCode + "]");
            extentTest.get().log(Status.PASS, "Response Intercepted. Status Code: [" + statusCode + "]");

            return this;

        } catch (Exception e) {
            log.error("Runtime Infrastructure Exception inside HTTP Lifecycle: " + e.getMessage());
            extentTest.get().log(Status.FAIL, "HTTP Exception Root Cause: " + e.getMessage());
            throw e;
        } finally {
            initializeRequest(); // Clear structural specifications for potential multi-step calls
        }
    }

    // =========================================================================
    // STEP 4: FLUENT ASSERTION ENGINE (Validations chained on active context)
    // =========================================================================

    private void verifyResponseContextState() {
        if (lastResponseThread.get() == null) {
            throw new IllegalStateException("[FATAL] Evaluation failed: No active response packet found. Execute an HTTP verb method first.");
        }
    }

    public APIClient validateStatusCode(int expectedStatus) {
        verifyResponseContextState();
        int actualStatus = lastResponseThread.get().getStatusCode();
        try {
            lastResponseThread.get().then().statusCode(expectedStatus);
            log.info("Assertion Passed: HTTP Status Code verified as [" + actualStatus + "]");
            extentTest.get().log(Status.PASS, "Assert Status Code | Expected: [" + expectedStatus + "] | Actual: [" + actualStatus + "]");
        } catch (AssertionError e) {
            String errorMsg = "HTTP Status Mismatch! Expected: [" + expectedStatus + "] but verified: [" + actualStatus + "]";
            log.error(errorMsg);
            extentTest.get().log(Status.FAIL, errorMsg);
            throw e;
        }
        return this;
    }

    public APIClient validateResponseTime(long maxTimeMillis) {
        verifyResponseContextState();
        long actualTime = lastResponseThread.get().getTime();
        try {
            lastResponseThread.get().then().time(Matchers.lessThanOrEqualTo(maxTimeMillis));
            log.info("Performance Metric Passed: Response Time clocked at [" + actualTime + "ms]");
            extentTest.get().log(Status.PASS, "Assert Response SLA | Threshold: [" + maxTimeMillis + "ms] | Real Time: [" + actualTime + "ms]");
        } catch (AssertionError e) {
            String errorMsg = "Performance SLA Breach! Response Time [" + actualTime + "ms] violated maximum constraint of [" + maxTimeMillis + "ms]";
            log.error(errorMsg);
            extentTest.get().log(Status.FAIL, errorMsg);
            throw e;
        }
        return this;
    }

    public APIClient validateJsonField(String jsonKey, Object expectedValue) {
        verifyResponseContextState();
        Object actualValue = jsonPathThread.get().get(jsonKey);
        
        if (Objects.equals(actualValue, expectedValue)) {
            log.info("JSON Path Assertion Passed: Field [" + jsonKey + "] holds expected data payload: " + expectedValue);
            extentTest.get().log(Status.PASS, "Assert JSON Node [" + jsonKey + "] | Found: " + expectedValue);
        } else {
            String errorMsg = String.format("JSON Element Value Deviation! Key: [%s] | Expected: %s | Intercepted: %s", jsonKey, expectedValue, actualValue);
            log.error(errorMsg);
            extentTest.get().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg);
        }
        return this;
    }

    public APIClient validateFieldExists(String jsonKey) {
        verifyResponseContextState();
        if (jsonPathThread.get().get(jsonKey) != null) {
            log.info("JSON Node Structural Verification: Element Path [" + jsonKey + "] exists.");
            extentTest.get().log(Status.PASS, "Verified absolute existence of structural key node: [" + jsonKey + "]");
        } else {
            String errorMsg = "Structural Integrity Failure! Expected JSON node element mapping [" + jsonKey + "] was completely missing.";
            log.error(errorMsg);
            extentTest.get().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg);
        }
        return this;
    }

    public APIClient validateBodyContains(String expectedText) {
        verifyResponseContextState();
        if (lastResponseThread.get().asString().contains(expectedText)) {
            log.info("Raw Document Check: Substring sequence [" + expectedText + "] localized inside body packet.");
            extentTest.get().log(Status.PASS, "Verified target text payload mapping exists: " + expectedText);
        } else {
            String errorMsg = "Document Substring Validation Failure! Sequence [" + expectedText + "] missing inside body stream.";
            log.error(errorMsg);
            extentTest.get().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg);
        }
        return this;
    }

    public APIClient validateHeader(String headerName, String expectedValue) {
        verifyResponseContextState();
        String actualValue = lastResponseThread.get().getHeader(headerName);
        if (expectedValue.equalsIgnoreCase(actualValue)) {
            log.info("Metadata Header Match: [" + headerName + "] established connection data content = [" + actualValue + "]");
            extentTest.get().log(Status.PASS, "Assert Protocol Header [" + headerName + "] | Extracted: " + actualValue);
        } else {
            String errorMsg = String.format("HTTP Header Mismatch for Field Node [%s] | Expected: %s | Verified: %s", headerName, expectedValue, actualValue);
            log.error(errorMsg);
            extentTest.get().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg);
        }
        return this;
    }

    /** Safely fetches the raw RestAssured response entity if extraction parsing is required later. */
    public Response getResponse() {
        verifyResponseContextState();
        return this.lastResponseThread.get();
    }

    /** Clears active threads from memory maps to avoid heap leaks. Must run inside teardown listeners. */
    public void cleanup() {
        requestSpecThread.remove();
        lastResponseThread.remove();
        jsonPathThread.remove();
    }
}
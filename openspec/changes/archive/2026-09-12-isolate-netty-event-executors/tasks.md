## 1. Provider API and Lifecycle

- [x] 1.1 Add the protocol-neutral `JtEventExecutorGroupProvider` API and its stable-instance default implementation in `jt-core`, and verify neither type is assignable to `ScheduledExecutorService` with focused unit tests.
- [x] 1.2 Implement explicit graceful shutdown for the default Provider and verify closing it shuts down the owned executor exactly once without changing the returned executor identity.

## 2. Named Auto-Configuration Integration

- [x] 2.1 Retain the existing 808 instruction, 808 attachment, and 1078 bean-name constants unchanged while documenting their new Provider type contract, and verify all affected support modules compile without new or deprecated replacement constants.
- [x] 2.2 Replace the 808 instruction and attachment raw executor beans with named default Provider beans, inject the qualified Providers into built-in Netty configuration, and verify both 808 auto-configuration paths compile.
- [x] 2.3 Replace the 1078 raw executor bean with its named default Provider bean, inject the qualified Provider into built-in Netty configuration, and verify the 1078 auto-configuration path compiles.
- [x] 2.4 Update the customized 808 sample to inject and unwrap the named Provider using the existing bean-name constant, and verify the sample compiles.

## 3. Regression Coverage

- [x] 3.1 Add 808 auto-configuration context tests for instruction and attachment Provider creation, named custom-Provider back-off, qualified executor selection, multiple-Provider isolation, and absence of framework-owned `ScheduledExecutorService` beans; verify the new test class passes.
- [x] 3.2 Add equivalent 1078 auto-configuration context tests for default creation, named custom-Provider back-off, qualified selection, and scheduler-type isolation; verify the new test class passes.
- [x] 3.3 Add starter-level scheduling regression tests for the supported Spring Boot 2 and Spring Boot 3 variants that enable `@Scheduled` processing and assert Spring selects its own `TaskScheduler` while JT/T Providers are present; verify both variant test tasks pass.
- [x] 3.4 Close test application contexts and assert default Provider executors enter graceful shutdown while custom externally managed Provider executors are not independently destroyed by auto-configuration; verify lifecycle tests pass without lingering executor threads.

## 4. Documentation and Migration

- [x] 4.1 Document that the three existing bean names now identify Provider beans, along with the stable-instance/lifecycle contract and a custom Provider example; verify documentation links and code identifiers match the implemented API.
- [x] 4.2 Add a migration note showing that raw `EventExecutorGroup` injection or override changes type but retains its qualifier constant, and verify repository samples use the existing constants with Provider types.

## 5. Verification

- [x] 5.1 Run targeted `jt-core`, 808 auto-configuration/starter, 1078 auto-configuration/starter, and customized-sample test or compile tasks for both compatibility variants, and verify all targeted Gradle tasks succeed.
- [x] 5.2 Run the repository before-commit checks and `openspec validate isolate-netty-event-executors --strict`, resolving any failures before marking the change complete.

## 6. Migration Failure Analysis

- [x] 6.1 Add protocol-specific Spring Boot `FailureAnalyzer` implementations for legacy named `EventExecutorGroup` beans, register them through `META-INF/spring.factories`, and ensure precise matching leaves unrelated failures to Spring Boot.
- [x] 6.2 Add focused 808 instruction, 808 attachment, and 1078 migration-analysis tests, including false-positive guards and Spring factories registration checks; verify the affected modules pass on the shared Boot 2-compatible API.
- [x] 6.3 Document the expected migration failure and remediation, then rerun targeted Boot 2/3 tests, repository before-commit checks, and strict OpenSpec validation.

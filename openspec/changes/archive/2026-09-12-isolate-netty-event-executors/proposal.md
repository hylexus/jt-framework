## Why

The JT/T auto-configuration exposes Netty message-processing `EventExecutorGroup` instances as Spring beans. Because Netty's type also implements `ScheduledExecutorService`, Spring Boot can mistake these internal executors for the application's task scheduler, route `@Scheduled` work onto them, and fail startup when Spring supplies a small negative recurring-task delay that Netty rejects.

## What Changes

- Introduce a JT-specific event-executor-group Provider abstraction that exposes the Netty executor without itself implementing `EventExecutorGroup` or `ScheduledExecutorService`.
- Change the existing named beans for the JT/T 808 instruction server, JT/T 808 attachment server, and JT/T 1078 server to Provider type while retaining their bean names and public bean-name constants.
- Preserve ownership and graceful shutdown of the auto-configured executor through the Provider lifecycle.
- Verify that enabling JT/T servers no longer suppresses Spring Boot's default `TaskScheduler` or captures application `@Scheduled` tasks.
- Provide a targeted Spring Boot failure analysis when a legacy named `EventExecutorGroup` bean prevents the new Provider bean from being auto-configured.
- Update customization examples and documentation to demonstrate overriding the Provider.
- **BREAKING**: Change the existing named beans from `EventExecutorGroup` to Provider type; consumers that inject or override the raw executor beans must migrate their bean type while continuing to use the same bean name.

## Capabilities

### New Capabilities

- `netty-executor-isolation`: Defines how JT/T message-processing executors are provided, customized, isolated from Spring task scheduling, and shut down.

### Modified Capabilities

None.

## Impact

- Affected modules: `jt-core`, JT/T 808 and JT/T 1078 Spring Boot auto-configuration/support modules, and relevant samples or documentation.
- Affected API: the existing bean-name constants remain stable, while the bean and dependency types used by built-in/custom Netty configuration change to Provider.
- Runtime effect: Spring Boot remains responsible for choosing the application task scheduler; JT/T message-processing executors remain dedicated to Netty pipelines.
- Migration diagnostics: applications retaining a legacy named executor bean receive an actionable Provider migration message with a link to issue #101.
- Compatibility: both Spring Boot 2 and Spring Boot 3 variants must continue to compile and behave consistently after the API migration.

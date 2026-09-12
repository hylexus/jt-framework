## Purpose

Ensure JT/T message-processing executors remain dedicated Netty infrastructure while allowing applications to customize them without affecting Spring's application task scheduling.

## ADDED Requirements

### Requirement: Internal executor isolation
The JT/T auto-configuration SHALL expose each framework-created message-processing executor only through a Provider type that is not assignable to `EventExecutorGroup` or `ScheduledExecutorService`.

#### Scenario: Spring scheduling is enabled with an 808 instruction server
- **WHEN** an application enables Spring scheduled tasks and the auto-configured JT/T 808 instruction server
- **THEN** the JT/T message-processing executor is not a Spring `ScheduledExecutorService` candidate
- **AND** Spring Boot can create and select its application `TaskScheduler`

#### Scenario: Spring scheduling is enabled with an 808 attachment server
- **WHEN** an application enables Spring scheduled tasks and the auto-configured JT/T 808 attachment server
- **THEN** the attachment message-processing executor is not a Spring `ScheduledExecutorService` candidate
- **AND** application scheduled tasks are not submitted to that executor

#### Scenario: Spring scheduling is enabled with a 1078 server
- **WHEN** an application enables Spring scheduled tasks and the auto-configured JT/T 1078 server
- **THEN** the JT/T 1078 message-processing executor is not a Spring `ScheduledExecutorService` candidate
- **AND** application scheduled tasks are not submitted to that executor

### Requirement: Provider-based customization
The framework SHALL retain the existing distinct bean name and public bean-name constant for each JT/T 808 instruction, JT/T 808 attachment, and JT/T 1078 message-processing executor while changing the bean value to a Provider, and each built-in Netty pipeline SHALL use the executor returned by its corresponding Provider.

#### Scenario: Existing bean identifiers remain stable
- **WHEN** an application migrates from a raw executor bean to a Provider bean
- **THEN** it uses the same server-specific bean name and public bean-name constant as before
- **AND** only the declared and injected bean type changes

#### Scenario: Application supplies a custom Provider
- **WHEN** an application declares a custom Provider under a server's existing documented executor bean name
- **THEN** auto-configuration backs off its default Provider for that server
- **AND** the corresponding Netty pipeline uses the custom Provider's executor

#### Scenario: Multiple JT/T servers are enabled
- **WHEN** multiple supported JT/T server types are enabled in one application
- **THEN** each server resolves its own named Provider
- **AND** no server consumes another server's message-processing executor

### Requirement: Managed executor lifecycle
The default Provider SHALL retain one stable executor instance for its lifetime, and the framework SHALL gracefully shut down that framework-created executor when the application context closes.

#### Scenario: Default Provider is resolved repeatedly
- **WHEN** framework components request the executor from the same default Provider more than once
- **THEN** the Provider returns the same executor instance

#### Scenario: Application context closes
- **WHEN** the application context containing a default Provider is closed
- **THEN** its framework-created message-processing executor begins graceful shutdown

### Requirement: Supported Spring Boot compatibility
Provider-based executor isolation SHALL behave consistently in both supported Spring Boot 2 and Spring Boot 3 starter variants.

#### Scenario: Starter compatibility verification
- **WHEN** the relevant auto-configuration tests run against either supported starter variant
- **THEN** the Provider customization contract, executor isolation, and lifecycle expectations are satisfied

### Requirement: Legacy bean migration diagnostics
The framework SHALL provide an actionable Spring Boot failure analysis when a legacy raw `EventExecutorGroup` bean uses one of the retained message-processing bean names and therefore prevents the corresponding Provider from being auto-configured.

#### Scenario: Legacy 808 instruction executor bean blocks startup
- **WHEN** the application declares an `EventExecutorGroup` under the retained 808 instruction executor bean name
- **AND** built-in 808 Netty configuration requires the corresponding `JtEventExecutorGroupProvider`
- **THEN** startup failure analysis identifies the legacy bean type and retained bean name
- **AND** the action instructs the user to replace the bean value with a `JtEventExecutorGroupProvider`
- **AND** the analysis links to issue #101

#### Scenario: Legacy attachment or 1078 executor bean blocks startup
- **WHEN** the application declares an `EventExecutorGroup` under the retained 808 attachment or 1078 executor bean name
- **THEN** the corresponding protocol-specific failure analyzer provides the same actionable migration guidance

#### Scenario: Unrelated Provider lookup fails
- **WHEN** a `JtEventExecutorGroupProvider` lookup failure does not involve a retained bean name backed by an `EventExecutorGroup`
- **THEN** the migration analyzer returns no analysis
- **AND** Spring Boot's normal failure analysis remains responsible for the error

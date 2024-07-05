# StaffTracker

StaffTracker is an out-of-the-box backend solution for staff tracking and management.

Implemented using a fully containerized architecture, it allows anyone with docker installed
on their system to get up and running in a matter of seconds. Key features include:
- Department management
- Employee management

## Architecture

The application has been built from the ground up using a microservices architecture with
Java based Spring Boot applications. The applications communicate with each other either through
HTTP REST calls or through asynchronous messaging depending on the use case

### Services

There are a total of 14 services containing 4 core microservices and 10 other supporting services
spanning concerns like persistence, messaging, logging and monitoring.

#### Microservices

| service    | description        |
|:-----------|:-------------------|
| eureka     | discovery server   |
| gateway    | API gateway        |
| department | manage departments |
| employee   | manage employees   |

#### Databases

| service       | description                     |
|:--------------|:--------------------------------|
| department-db | department database service     |
| employee-db   | employee database service       |
| adminer       | database administration service |

#### Messaging

| service  | description                             |
|:---------|:----------------------------------------|
| rabbitmq | asynchronous messaging between services |

#### Logging

| service       | description                         |
|:--------------|:------------------------------------|
| elasticsearch | log storage and indexing            |
| logstash      | log processing pipeline             |
| kibana        | visualization and dashboard service |
| filebeat      | log forwarding service              |

#### Monitoring

| service    | description                         |
|:-----------|:------------------------------------|
| prometheus | monitoring service                  |
| grafana    | analytics and visualization service |

### Event-Driven Architecture

*RabbitMQ* has been used for asynchronous communication between the services. This is useful
when a request does not need to be processed immediately or takes significant amount of time
to process that it doesn't make sense to wait for it using a synchronous request.

In the context of this application, when deleting departments, the `department` service sends a message
to the `employee` service initiating the deletion of employees belonging to the deleted department.

```text
             departmentDeletedEvents
department ---------------------------> employee
                     RabbitMQ
```

### Logging and Analysis

The **ELK stack** (*Elasticsearch, Logstash, Kibana*) has been used here along with *Filebeat* for centralized logging.

The logs are collected by *Filebeat* from the docker containers and sent over to the *Logstash* pipeline
where they are processed before sending them over to *Elasticsearch* for storage and indexing.
The stored logs are then visualized by *Kibana* and can be searched and filtered through using its UI.

```text
Filebeat -> Logstash -> Elasticsearch -> Kibana
```

### Monitoring and Observability

*Prometheus* has been used for monitoring the services. It coordinates with the `eureka` discovery
service to collect metrics from all running services and their instances.

*Grafana* then uses this *Prometheus* instance as a datasource and visualizes the
collected metrics using its dashboards.

## Prerequisites

- [Docker](https://www.docker.com/)

## Build Instructions

1. Clone the repository:
    ```bash
    git clone https://github.com/dwrik/StaffTracker.git
    cd StaffTracker
    ```

2. Create a `.env` file in the root directory with the following variables:
    ```env
    # Database username for stafftracker
    DB_USERNAME=
    # Database password for stafftracker
    DB_PASSWORD=
    # Database name for stafftracker department service
    DB_NAME_DEPARTMENT=
    # Database name for stafftracker employee service
    DB_NAME_EMPLOYEE=
    # RabbitMQ username for stafftracker
    RABBITMQ_USER=
    # RabbitMQ password for stafftracker
    RABBITMQ_PASSWORD=

    # Password for the 'elastic' user (at least 6 characters)
    ELASTIC_PASSWORD=
    # Password for the 'kibana_system' user (at least 6 characters)
    KIBANA_PASSWORD=
    # Version of Elastic products
    ES_STACK_VERSION=
    # Set Elasticsearch license
    ES_LICENSE=basic
    # Port to expose Elasticsearch HTTP API to the host
    ES_PORT=
    # Port to expose Kibana to the host
    KIBANA_PORT=
    ```

3. Run the containers:
    ```bash
    docker compose up
    ```

4. To stop the containers:
    ```bash
    docker compose down
    ```

## Future Scope

- OpenAPI swagger documentation of the APIs.
- Code coverage badges in the README file.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

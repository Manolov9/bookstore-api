Bookstore API Automation Tests
This project contains automated API tests for the FakeRestAPI focusing on the Books and Authors endpoints. The tests are implemented using RestAssured and JUnit 5, and are configured with Maven for build and dependency management.

Table of Contents
Project Overview

Technologies Used

Project Structure

Setup Instructions

Running Tests

Test Details

Test Ordering

Continuous Integration

Contributing

License

Project Overview
This test suite automates common CRUD operations for the Books and Authors resources exposed by the FakeRestAPI service. It covers:

GET all and by ID

POST (create)

PUT (update)

DELETE

Tests are designed to be clean and isolated, using JUnit lifecycle methods (@BeforeEach, @AfterEach) to prepare and clean data as needed.

Technologies Used
Java 17+ (recommended)

RestAssured 5.x

JUnit 5 (Jupiter)

Maven 3.8+

Hamcrest for assertions

Project Structure
bash
Copy
Edit
src/test/java/
 ├─ authors/            # Tests for Authors endpoint
 ├─ books/              # Tests for Books endpoint
 └─ config/             # Common test configuration (e.g., base URI)
Setup Instructions
Prerequisites

Java 17 or higher installed and configured in your PATH.

Maven installed and configured in your PATH.

Clone the repository

bash
Copy
Edit
git clone https://github.com/yourusername/bookstore-api-tests.git
cd bookstore-api-tests
Build the project and download dependencies

bash
Copy
Edit
mvn clean compile
Running Tests
Run all tests using Maven:

bash
Copy
Edit
mvn test
Alternatively, you can run tests from your favorite IDE that supports JUnit 5.

Test Details
Books Tests
Located in books/ package. Tests cover retrieval, creation, update, and deletion of book resources.

Authors Tests
Located in authors/ package. Similar coverage for authors resources.

Tests use JUnit 5 lifecycle annotations (@BeforeEach, @AfterEach) to create and clean test data for isolation.

Tests requiring specific execution order are annotated with JUnit 5's @TestMethodOrder and @Order.

Test Ordering
To ensure data consistency and test independence, some tests require ordered execution:

Tests that update or delete created resources use @TestMethodOrder(MethodOrderer.OrderAnnotation.class) with explicit @Order annotations.

Each test creates its own data in @BeforeEach and cleans up in @AfterEach to avoid side effects.

Continuous Integration
This project includes a GitHub Actions workflow to automate testing on every push or pull request:

yaml
Copy
Edit
name: API Tests CI

on: [push, pull_request]

permissions:
  checks: write
  contents: read

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'

      - name: Build and Test
        run: mvn clean test

      - name: Publish JUnit Report
        uses: dorny/test-reporter@v1
        if: always()
        with:
          name: JUnit Report
          path: target/surefire-reports/*.xml
          reporter: java-junit
This workflow:

Checks out the code

Sets up Java 11 (Temurin distribution)

Builds and runs tests using Maven

Publishes JUnit reports for easy viewing in the GitHub UI

Contributing
Feel free to fork the project, add more tests for other endpoints, or improve the framework structure. Pull requests are welcome!

License
This project is licensed under the MIT License.

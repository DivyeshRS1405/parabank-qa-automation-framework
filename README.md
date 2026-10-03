# ParaBank QA Automation Framework

A practical Java-based QA automation framework built for the ParaBank demo banking application. The framework covers UI automation, API automation, data-driven testing, reusable utilities, and database validation using a maintainable Page Object Model architecture.

The project is designed as a portfolio project to demonstrate practical QA automation skills including test design, reusable framework components, positive and negative testing, API validation, and Maven-based test execution.

---

## Application Under Test

**ParaBank** — Parasoft's public demo banking application.

- Application: https://parabank.parasoft.com/parabank/index.htm
- API Base URL: `https://parabank.parasoft.com/parabank/services/bank`

---

## Technology Stack

| Category | Technology |
|---|---|
| Programming Language | Java 21 |
| Build Tool | Maven |
| UI Automation | Selenium WebDriver |
| Test Framework | TestNG |
| API Automation | REST Assured |
| Database Testing | JDBC + H2 |
| Design Pattern | Page Object Model (POM) |
| Browser Support | Chrome, Firefox, Edge |
| Version Control | Git & GitHub |
| IDE | Eclipse |
| CI/CD | GitHub Actions |
| Reporting | ExtentReports |

---

# Framework Architecture

```text
parabank-qa-automation-framework
│
├── src
│   │
│   ├── main
│   │   ├── java
│   │   │   └── com.divyesh.framework
│   │   │       ├── config
│   │   │       │   └── ConfigReader
│   │   │       │
│   │   │       ├── driver
│   │   │       │   └── DriverFactory
│   │   │       │
│   │   │       ├── pages
│   │   │       │   ├── BasePage
│   │   │       │   ├── LoginPage
│   │   │       │   ├── RegisterPage
│   │   │       │   ├── HomePage
│   │   │       │   └── Other Page Objects
│   │   │       │
│   │   │       └── utils
│   │   │           ├── WaitUtils
│   │   │           ├── TestDataFactory
│   │   │           └── RegistrationHelper
│   │   │
│   │   └── resources
│   │       └── config.properties
│   │
│   └── test
│       │
│       ├── java
│       │   └── com.divyesh.tests
│       │       ├── base
│       │       │   ├── BaseTest
│       │       │   └── ApiBaseTest
│       │       │
│       │       ├── ui
│       │       │   ├── LoginTest
│       │       │   ├── RegistrationTest
│       │       │   └── Other UI Tests
│       │       │
│       │       └── api
│       │           ├── CustomerApiTest
│       │           ├── AccountApiTest
│       │           └── LoanApiTest
│       │
│       └── resources
│
├── pom.xml
├── README.md
└── .gitignore
Framework Design
Page Object Model

The framework follows the Page Object Model (POM) design pattern.

Each major application page has its own Java class containing:

Web element locators
Page-specific actions
Page-level validations

Example:

LoginPage
RegisterPage
HomePage
AccountsOverviewPage
TransferFundsPage
BillPayPage
RequestLoanPage
UpdateProfilePage

This keeps test cases focused on business scenarios instead of low-level Selenium operations.

Benefits
Reduces code duplication
Improves maintainability
Centralizes locators
Makes test cases easier to read
Simplifies future application changes
Core Framework Components
BaseTest

BaseTest provides common UI test setup and teardown.

Responsibilities:

Initialize WebDriver
Open the browser
Configure the browser
Close the browser after execution
ApiBaseTest

ApiBaseTest provides common REST Assured configuration.

The API base URI is loaded from the framework configuration instead of being hard-coded inside every test class.

DriverFactory

DriverFactory centralizes browser creation.

Supported browsers:

Chrome
Firefox
Edge

The framework also supports headless execution through configuration.

Example:

browser=chrome
headless=false

Headless execution:

mvn clean test -Dheadless=true
WaitUtils

WaitUtils provides reusable explicit-wait methods.

Examples include:

Wait for visibility
Wait for clickability
Wait for element presence
Wait for URL
Wait for page title
Wait for visible text
Wait for select-option availability

This avoids unnecessary hard-coded sleeps and improves synchronization between the test and application.

TestDataFactory

TestDataFactory generates dynamic test data.

Examples:

Unique usernames
Random phone numbers
Random SSNs
Registration data
Payee data

Unique usernames are generated at runtime so that registration tests do not depend on previously created accounts.

RegistrationHelper

RegistrationHelper provides a reusable way to create a temporary ParaBank customer.

This is particularly useful for API tests because ParaBank does not expose a direct customer-creation REST endpoint.

The helper:

Opens ParaBank
Generates unique customer data
Registers the customer through the UI
Returns the generated customer information
Automated Modules
1. Login

Covered scenarios include:

Valid login
Invalid username/password
Invalid password
Empty username
Empty password
Empty credentials
Logout
2. Customer Registration

Covered scenarios include:

Valid registration
Required-field validation
Empty required fields
Password confirmation mismatch
Dynamic username generation
3. Accounts

Covered areas include:

Accounts Overview
Account information
Account activity / transaction history
Opening a new account
4. Transfer Funds

Covered areas include:

Valid fund transfer
Transfer amount validation
Balance-related validation
5. Bill Payment

Covered areas include:

Valid bill payment
Required-field validation
Amount validation
6. Request Loan

Covered areas include:

Valid loan request
Approval/denial response
Invalid account scenario
Loan input validation
7. Update Profile

Covered scenarios include:

Valid profile update
Required-field validation
API Automation

REST Assured is used for API automation and response validation.

API Base URL
https://parabank.parasoft.com/parabank/services/bank
API Endpoints Covered
Method	Endpoint	Purpose
GET	/customers/{customerId}	Retrieve customer
GET	/customers/{customerId}/accounts	Retrieve customer accounts
GET	/accounts/{accountId}	Retrieve account
GET	/login/{username}/{password}	API login
POST	/createAccount	Create account
POST	/deposit	Deposit money
POST	/withdraw	Withdraw money
POST	/transfer	Transfer funds
POST	/requestLoan	Request loan
API Test Data Strategy

ParaBank does not provide a direct REST endpoint for creating a new customer.

Therefore, API tests use the following approach:

UI Registration
       ↓
Generate unique customer
       ↓
Resolve customer ID through API login
       ↓
Execute API tests
       ↓
Validate API response

This allows API tests to work with dynamically created customers instead of depending on fixed demo accounts.

Database Testing

The project includes a JDBC-based database validation layer using a local H2 database.

The database tests demonstrate:

JDBC connection
SQL queries
PreparedStatement usage
Data validation
Update operations
Cleanup

The database tests use a local in-memory H2 database rather than ParaBank's production database.

This is intentional because ParaBank's live database is not publicly accessible.

Data-Driven Testing

TestNG @DataProvider is used for scenarios that require multiple sets of test data.

Examples include:

Invalid login credentials
Registration validation
Profile update validation

Example:

@DataProvider(name = "invalidCredentials")
public Object[][] invalidCredentials() {
    return new Object[][] {
        {"invalidUser", "invalidPassword"},
        {"unknownUser", "WrongPassword123!"}
    };
}

This allows the same test logic to execute against multiple datasets.

Test Execution
Requirements

Before running the project, install:

JDK 21
Maven
Chrome / Firefox / Edge
Git

Selenium Manager automatically handles browser driver management, so manual WebDriver executable configuration is not required.

Run Complete Test Suite
mvn clean test
Run in Headless Mode
mvn clean test -Dheadless=true
Run with a Different Browser

Chrome:

mvn clean test -Dbrowser=chrome

Firefox:

mvn clean test -Dbrowser=firefox

Edge:

mvn clean test -Dbrowser=edge
Run a Specific Test Class

Example:

mvn clean test -Dtest=LoginTest
Configuration

Application configuration is maintained in:

src/main/resources/config.properties

Example:

browser=chrome
headless=false

baseUrl=https://parabank.parasoft.com/parabank/index.htm

apiBaseUrl=https://parabank.parasoft.com/parabank/services/bank

explicitWait=10

Keeping configuration separate from test logic makes the framework easier to maintain and execute in different environments.

Test Results

Latest full Maven execution:

Tests run: 47
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS

The complete suite currently contains 47 automated tests, and the latest full execution completed successfully.

Reports

Depending on the configured reporting components, generated test artifacts are available under:

reports/
test-output/
target/surefire-reports/

Typical outputs include:

reports/html/
reports/screenshots/
test-output/
target/surefire-reports/
CI/CD

The project is designed to support automated execution through GitHub Actions.

A CI workflow can execute:

mvn clean test -Dheadless=true

and publish test execution artifacts such as:

Test reports
Screenshots
Surefire results
Project Highlights

The framework demonstrates the following QA automation concepts:

Java-based automation
Selenium WebDriver
TestNG
Maven
Page Object Model
Reusable BaseTest
Driver Factory
ThreadLocal WebDriver
Explicit waits
Dynamic test data
Data-driven testing
UI automation
REST API automation
Positive testing
Negative testing
Validation testing
XML response validation
JDBC database validation
Configuration-driven execution
Git/GitHub workflow
CI/CD-ready structure
Limitations
ParaBank Data Reset

ParaBank is a public demo application and its data can be reset periodically.

Therefore, the framework does not depend on a fixed pre-existing customer.

Test customers are generated dynamically during execution.

Customer Creation API

ParaBank does not expose a direct REST endpoint for creating a customer.

API tests therefore create customers through the UI and subsequently resolve the customer ID through the login API.

Database Validation

Database tests use a local in-memory H2 database.

They do not connect to ParaBank's production database.

Find Transactions

The Find Transactions page is available in the application and can be navigated through the UI.

The multi-criteria Find Transactions search workflow is not fully automated in the current version.

Account activity / transaction history is covered through the Accounts Overview flow.

Future Improvements


Possible future enhancements include:

Additional API coverage
More database validation scenarios
Automatic screenshots for failed tests
Retry mechanism
Parallel test execution
Extended cross-browser execution
Enhanced ExtentReports dashboard
More CI/CD pipeline stages
Additional negative test scenarios

Author
Divyesh Sonawane



Technical Skills
Java
Selenium WebDriver
TestNG
REST Assured
SQL
JDBC
Maven
Git
GitHub
API Testing
UI Automation
Manual Testing
License

This project is created for learning, portfolio development, and demonstration of QA automation engineering practices.
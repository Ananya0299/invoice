# Invoice System


This Simple Invoice System provides APIs to create invoices, pay invoices, and process overdue invoices.


## API Endpoints


### Create Invoice
This API is used to create a new invoice. It accepts invoice details as input and returns the unique identifier of the created invoice.
```http


Endpoint : POST /invoices


Request Body :
{
 "amount" : 150.50,
 "dueDate" : "2024-06-29"
}


Response :
Status : 201 Created
Body :
{
 "id": "1234"
}
```


### Get All Invoice
The API getAllInvoices is required to fetch all invoice records from the database.
```http
Endpoint : GET /invoices


Response :
Status : 200 OK
Body :
{
   "message": "Invoices",
   "data": [
       {
           "id": 1,
           "amount": 500.55,
           "paidAmount": 0.0,
           "dueDate": "2024-06-05",
           "status": "pending"
       }
   ]
}


```
### Process Payment
Processes a payment for a given invoice. This method updates the paid amount of the invoice and sets its status to PAID if the total paid amount matches the invoice amount.
```http
Endpoint : POST /invoices/{invoiceId}/payments


Request Body :
{
 "amount": 159.99
}


Response :
Status: 200 OK if the payment is successful


```


### Process Overdue Payment
This endpoint processes all pending invoices that are overdue. It applies a late fee to invoices that are partially paid and creates new invoices for the remaining amount plus the late fee. Invoices that are fully paid are marked as paid.
```http
Endpoint : POST /invoices/process-overdue


{
   "lateFee": 100.00,
   "overdueDays": 10
}
Response :
Status : 200 OK if the processing is successful
```
### Example Usage
### Create an Invoice
```http


curl -X POST http://localhost:8080/invoices -H "Content-Type: application/json" -d '{"amount": 199.99, "due_date": "2021-09-11"}'


```
### Get All Invoices
```http


curl -X GET http://localhost:8080/invoices


```
### Pay an Invoice
```http


curl -X POST http://localhost:8080/invoices/1234/payments -H "Content-Type: application/json" -d '{"amount": 159.99}'


```
### Process Overdue Invoices
```http


curl -X POST http://localhost:8080/invoices/process-overdue -H "Content-Type: application/json" -d '{"late_fee": 10.5, "overdue_days": 10}'


```
### Requirements
```http


Java 8+
Spring Boot
Maven
Setup and Running


```
### Setup and Running
Clone the repository :
```http


git clone <repository-url>
cd <repository-directory>


```
Build the project :
```http


mvn clean install


```
Run the application :
```http


mvn spring-boot:run


```


The application will start on http://localhost:8080.


## Assumptions
* Payments cannot exceed the total amount due on an invoice


## Additional Functionality
* The system handles basic error cases, such as invalid invoice IDs or no overdue invoices exist.
* Appropriate HTTP status codes and error messages are returned for invalid requests.
* Unit tests are included for the controller, service, and DAO layers to ensure that core functionalities work as expected.
# DocumentFlow - automation tool for request documents
DocumentFlow is a web platform designed to automate and manage request documents and forms within companies, aiming to streamline internal processes and reduce processing times. It offers an intuitive, no-code environment with configurable workflows and customizable forms. Targeted at small and medium-sized businesses, DocumentFlow provides a ready-to-use solution built on robust technologies such as Spring, Redis, and PostgreSQL, with a modern interface developed using React, Next.js, and Ant Design.

## Main Features

### Workflow Modeler
The Workflow Modeler allows administrators to visually create and manage workflows using a drag-and-drop interface. They can define who can start a workflow, add predefined components (like approvals or conditional branches) to the diagram, and link them based on execution logic. Each step can be customized with properties and reference names for dynamic behavior.
<img width="1004" height="531" alt="image" src="https://github.com/user-attachments/assets/cfacc595-c176-4506-88c0-9541b9b9401c" />

### Form Builder
The Form Builder allows administrators to create and manage multi-page forms through a simple interface. They can add, edit, reorder, or remove fields with various data types (text, number, date, selection, etc.). Fields are added using a "+" button, and selection fields allow custom options. Forms can be structured across multiple pages for better organization.
<img width="1004" height="592" alt="image" src="https://github.com/user-attachments/assets/7e6e535b-7531-46e8-825e-ba79be20604c" />

### Workflow Interaction
Users (employees or clients) can start a workflow by submitting a form from the "Send Request" section. After submission, the workflow executes based on its defined logic. Only employees can interact further with others’ workflows.

Employees have access to several key sections:
 - Approvals: View and respond to approval requests assigned to them with "Approve" or "Reject".
 - Corrections/Completions: Modify form data when requested, then submit with "Send Reply".
 - Notifications: Read-only messages or form updates that don’t require a response.
 - Archive: View or delete past requests (no editing allowed); available only to employees.
<img width="1004" height="501" alt="image" src="https://github.com/user-attachments/assets/64b718e3-6618-4167-ae37-af9cab12dd71" />
<img width="1004" height="496" alt="image" src="https://github.com/user-attachments/assets/46e265b3-8a3e-4542-a6e9-0c435c0c4ea7" />
<img width="1004" height="510" alt="image" src="https://github.com/user-attachments/assets/90b614d9-052e-4b0e-92de-b1164b242a83" />
<img width="1004" height="499" alt="image" src="https://github.com/user-attachments/assets/b94712dd-5b2e-4d74-875e-31de84313462" />
<img width="1004" height="497" alt="image" src="https://github.com/user-attachments/assets/4efda6c2-b0fd-435c-8a8d-a8d8578dd667" />

## Use Case Example

### Leave Request Workflow:

 1. The employee accesses the "Leave Request" form, fills in personal details, desired time period, and reason, then submits it.
 2. The direct manager is notified that a leave request has been submitted.
 3. The manager receives a notification in the approvals section, where they can approve or reject the request.
 4. The decision is sent to the HR department and to the requester.

<img width="1004" height="619" alt="image" src="https://github.com/user-attachments/assets/aa1ce3f1-84b6-4e34-87a7-554d0dc551dc" /> 

### Budget Request Workflow:

 1. The manager submits the budget request form.
 2. The CFO reviews and approves the request.
 3. If the requested amount exceeds €5000, proceed to step 4; otherwise, go directly to step 6.
 4. The CEO gives additional approval.
 5. The finance department is notified that a new, approved budget request has been submitted.
 6. The request is archived.

<img width="1004" height="608" alt="image" src="https://github.com/user-attachments/assets/941b8f06-5d60-4bbf-9eca-7475634e6730" />


### Customer Support Request:

 1. The client submits the "Support Request" form, describing the issue and setting the priority level.
 2. A notification is sent to the "Level 1 Support" role to signal the new request.
 3. The priority is checked:
    - If "High", proceed to step 4.
    - If not, proceed directly to step 5.
 4. The "Support Team Leader" must approve the request:
    - If approved, continue to step 5.
    - If rejected, a notification is sent to the client informing them the request was not accepted.
 5. The "Level 1 Support" team is asked to complete the "Support Request Resolution" form once the issue is resolved.
 6. After the resolution confirmation form is submitted, it is archived along with the initial request.
 7. An email is sent to key company stakeholders to announce the resolution of a new support request.
 8. A confirmation notification is sent to the client indicating the request has been resolved.

<img width="1004" height="564" alt="image" src="https://github.com/user-attachments/assets/8477026d-ccdf-41fd-80ed-d932e3ab763e" />

window.onload = () =>{

    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");

    getEmployeeList();
}

function getEmployeeList() {
        //AJAX Logic
        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
                let data = JSON.parse(xhr.responseText);
                  console.log(data);
    
                displayEmployees(data);
            }
        };
          //Doing a HTTP to a specifc endpoint
          xhr.open("POST",`employee.do?num=3`);
          xhr.send();
}

function displayEmployees(data) {
   
    if(data.message) {
        document.getElementById("registrationMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong</span>';
    } else {

        let customerList = document.getElementById("employeeList");

        // Clean the customer list.
        customerList.innerHTML ="";

        // Iterate over all customers
        data.forEach((employee) => {
            // Creating node of <li>
            let tablerow = document.createElement("tr");   
            let tabledata1 = document.createElement("td");
            let tabledata2 = document.createElement("td");
            let tabledata3 = document.createElement("td");
            let tabledata4 = document.createElement("td");
            let tabledata5 = document.createElement("td");

            // Add class for styling <li class = "something">
            //customerNode.className = "list-group-item";

            // Getting innerHtml object, adding customer name to it.
            // <li class="something">[creating this object]</li>
            var id = document.createTextNode(`${employee.id}`);
            var name = document.createTextNode(`${employee.firstName} ${employee.lastName}`);
            var username = document.createTextNode(`${employee.username}`);
            var email = document.createTextNode(`${employee.email}`);
            var role = document.createTextNode(`${employee.employeeRole.type}`);

            //Append innerHTML to the customer node
            tabledata1.appendChild(id);
            tabledata2.appendChild(name);
            tabledata3.appendChild(username);
            tabledata4.appendChild(email);
            tabledata5.appendChild(role);

            //Finally, we append the new CustomerNode to the CustomerList
            //<ul id="CustomerList">
            //<li class ="something">
            tablerow.appendChild(tabledata1);
            tablerow.appendChild(tabledata2);
            tablerow.appendChild(tabledata3);
            tablerow.appendChild(tabledata4);
            tablerow.appendChild(tabledata5);

            customerList.appendChild(tablerow);
        });
    } 
}
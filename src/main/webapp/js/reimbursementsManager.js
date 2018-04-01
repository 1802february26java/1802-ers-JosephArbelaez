window.onload = () =>{

    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");

    document.getElementById("pending").addEventListener("click", getAllPendingReimbursements);

    document.getElementById("finalized").addEventListener("click", getAllFinalizedReimbursements);

    getPopulateDropdown();

    document.getElementById("all").addEventListener("click", getUserPendingReimbursements);
}

function getPopulateDropdown(){
    //AJAX Logic
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
            let data = JSON.parse(xhr.responseText);
              console.log(data);

            populate(data);
        }
    };
      //Doing a HTTP to a specifc endpoint
      xhr.open("POST",`getAllEmployees.do?num=2`);
      xhr.send();
  }

function populate(data) {
    let ddlEmployee = document.getElementById("ddlEmployee");

    // Clean the customer list.
    //list.innerHTML ="";

    // Iterate over all customers
    data.forEach((employee) => {
        let option = document.createElement("option");
        option.setAttribute("value", `${employee.id}`)
        var emp = document.createTextNode(`${employee.lastName}, ${employee.firstName}`);
        option.appendChild(emp);
        ddlEmployee.appendChild(option);
    });
}

function getAllPendingReimbursements(){
    //AJAX Logic
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
            let data = JSON.parse(xhr.responseText);
              console.log(data);

            displayPendingReimbursements(data);
        }
    };
      //Doing a HTTP to a specifc endpoint
      xhr.open("POST",`reimbursements.do?fetch=pending&eid=0`);
      xhr.send();
  }

  function getAllFinalizedReimbursements(){
    //AJAX Logic
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
            let data = JSON.parse(xhr.responseText);
              console.log(data);

            displayFinalizedReimbursements(data);
        }
    };
      //Doing a HTTP to a specifc endpoint
      xhr.open("POST",`reimbursements.do?fetch=finalized&eid=0`);
      xhr.send();
  }

  function getUserPendingReimbursements(){
    //AJAX Logic
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
            let data = JSON.parse(xhr.responseText);
              console.log(data);

            displayPendingReimbursements(data);
        }
    };
      //Doing a HTTP to a specifc endpoint
      xhr.open("POST",`reimbursements.do?eid=${ddlEmployee.value}`);
      xhr.send();
  }
function displayPendingReimbursements(data) {
    
    if(data.message) {
        document.getElementById("registrationMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong</span>';
    } else {

        let customerList = document.getElementById("pendingReimbursementsList");

        // Clean the customer list.
        customerList.innerHTML ="";

        
        // Iterate over all customers
        data.forEach((reimbursement) => {
            
            // Creating node of <li>
            let tablerow = document.createElement("tr");
            tablerow.id = `${reimbursement.id}`;   
            let tabledata1 = document.createElement("td");
            let tabledata2 = document.createElement("td");
            let tabledata3 = document.createElement("td");
            let tabledata4 = document.createElement("td");
            let tabledata5 = document.createElement("td");
            let tabledata6 = document.createElement("td");
            let tabledata7 = document.createElement("td");
            let tabledata8 = document.createElement("td");
            let tabledata9 = document.createElement("td");
            let tabledata10 = document.createElement("td");
            // Add class for styling <li class = "something">
            //customerNode.className = "list-group-item";

            // Getting innerHtml object, adding customer name to it.
            // <li class="something">[creating this object]</li>
            var id = document.createTextNode(`${reimbursement.id}`);
            var tApproved = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
            var tResolved = document.createTextNode("-");
            var requester = document.createTextNode(`${reimbursement.requester.lastName}, ${reimbursement.requester.firstName}`);
            var approver = document.createTextNode("-");
            var amount = document.createTextNode(`${reimbursement.amount}`);
            var description = document.createTextNode(`${reimbursement.description}`);
            var status = document.createTextNode(`${reimbursement.status.status}`);
            var type = document.createTextNode(`${reimbursement.type.type}`);
            
            var accept = document.createElement("input");
            accept.type = "button";
            accept.className = "btn button approve-button";
            accept.value = "A";
            accept.addEventListener("click", () => {
                update(3, tablerow.id);
            });
            var deny = document.createElement("input");
            deny.type = "button";
            deny.className = "btn button approve-button";
            deny.value = "D";
            deny.addEventListener("click", () => {
                update(2, tablerow.id);
            });

            //Append innerHTML to the customer node
            tabledata1.appendChild(id);
            tabledata2.appendChild(tApproved);
            tabledata3.appendChild(tResolved);
            tabledata4.appendChild(requester);
            tabledata5.appendChild(approver);
            tabledata6.appendChild(amount);
            tabledata7.appendChild(description);
            tabledata8.appendChild(status);
            tabledata9.appendChild(type);
            tabledata10.appendChild(accept);
            tabledata10.appendChild(deny);
            //Finally, we append the new CustomerNode to the CustomerList
            //<ul id="CustomerList">
            //<li class ="something">
            tablerow.appendChild(tabledata1);
            tablerow.appendChild(tabledata2);
            tablerow.appendChild(tabledata3);
            tablerow.appendChild(tabledata4);
            tablerow.appendChild(tabledata5);
            tablerow.appendChild(tabledata6);
            tablerow.appendChild(tabledata7);
            tablerow.appendChild(tabledata8);
            tablerow.appendChild(tabledata9);
            tablerow.appendChild(tabledata10);

            customerList.appendChild(tablerow);
        });
    }
}
function update(status, id) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            if (data.success) {
                tableRow.style.display = "none";
            }
        }
    };
    xhr.open("POST", `update.do?id=${id}&status=${status}`)
    xhr.send();
    setTimeout(() =>{ window.location.replace("viewAllReimbursementsManager.html");}, 1000);
}

function displayFinalizedReimbursements(data) {
    
    if(data.message) {
        document.getElementById("registrationMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong</span>';
    } else {

        let customerList = document.getElementById("pendingReimbursementsList");

        // Clean the customer list.
        customerList.innerHTML ="";

        // Iterate over all customers
        data.forEach((reimbursement) => {
            // Creating node of <li>
            let tablerow = document.createElement("tr");   
            let tabledata1 = document.createElement("td");
            let tabledata2 = document.createElement("td");
            let tabledata3 = document.createElement("td");
            let tabledata4 = document.createElement("td");
            let tabledata5 = document.createElement("td");
            let tabledata6 = document.createElement("td");
            let tabledata7 = document.createElement("td");
            let tabledata8 = document.createElement("td");
            let tabledata9 = document.createElement("td");
            // Add class for styling <li class = "something">
            //customerNode.className = "list-group-item";

            // Getting innerHtml object, adding customer name to it.
            // <li class="something">[creating this object]</li>
            var id = document.createTextNode(`${reimbursement.id}`);
            var tApproved = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
            var tResolved = document.createTextNode(`${reimbursement.resolved.year}-${reimbursement.resolved.monthValue}-${reimbursement.resolved.dayOfMonth}, ${reimbursement.resolved.hour}:${reimbursement.resolved.minute}:${reimbursement.resolved.second}`);
            var requester = document.createTextNode(`${reimbursement.requester.lastName}, ${reimbursement.requester.firstName}`);
            var approver = document.createTextNode(`${reimbursement.approver.lastName}, ${reimbursement.approver.firstName}`);
            var amount = document.createTextNode(`${reimbursement.amount}`);
            var description = document.createTextNode(`${reimbursement.description}`);
            var status = document.createTextNode(`${reimbursement.status.status}`);
            var type = document.createTextNode(`${reimbursement.type.type}`);
            //Append innerHTML to the customer node
            tabledata1.appendChild(id);
            tabledata2.appendChild(tApproved);
            tabledata3.appendChild(tResolved);
            tabledata4.appendChild(requester);
            tabledata5.appendChild(approver);
            tabledata6.appendChild(amount);
            tabledata7.appendChild(description);
            tabledata8.appendChild(status);
            tabledata9.appendChild(type);

            //Finally, we append the new CustomerNode to the CustomerList
            //<ul id="CustomerList">
            //<li class ="something">
            tablerow.appendChild(tabledata1);
            tablerow.appendChild(tabledata2);
            tablerow.appendChild(tabledata3);
            tablerow.appendChild(tabledata4);
            tablerow.appendChild(tabledata5);
            tablerow.appendChild(tabledata6);
            tablerow.appendChild(tabledata7);
            tablerow.appendChild(tabledata8);
            tablerow.appendChild(tabledata9);

            customerList.appendChild(tablerow);
        });
    }
}
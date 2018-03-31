window.onload = () =>{

    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");

    document.getElementById("pending").addEventListener("click", getAllPendingReimbursements);

    document.getElementById("finalized").addEventListener("click", getAllFinalizedReimbursements);
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
      xhr.open("POST",`reimbursements.do?fetch=finalized`);
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
            let tabledata1 = document.createElement("td");
            let tabledata2 = document.createElement("td");
            let tabledata3 = document.createElement("td");
            let tabledata4 = document.createElement("td");
            let tabledata5 = document.createElement("td");
            let tabledata6 = document.createElement("td");
            let tabledata7 = document.createElement("td");
            let tabledata8 = document.createElement("td");
            // Add class for styling <li class = "something">
            //customerNode.className = "list-group-item";

            // Getting innerHtml object, adding customer name to it.
            // <li class="something">[creating this object]</li>
            var id = document.createTextNode(`${reimbursement.id}`);
            var tApproved = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
            var tResolved = document.createTextNode("-");
            var approver = document.createTextNode("-");
            var amount = document.createTextNode(`${reimbursement.amount}`);
            var description = document.createTextNode(`${reimbursement.description}`);
            var status = document.createTextNode(`${reimbursement.status.status}`);
            var type = document.createTextNode(`${reimbursement.type.type}`);
            //Append innerHTML to the customer node
            tabledata1.appendChild(id);
            tabledata2.appendChild(tApproved);
            tabledata3.appendChild(tResolved);
            tabledata4.appendChild(approver);
            tabledata5.appendChild(amount);
            tabledata6.appendChild(description);
            tabledata7.appendChild(status);
            tabledata8.appendChild(type);

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

            customerList.appendChild(tablerow);
        });
    }
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
            // Add class for styling <li class = "something">
            //customerNode.className = "list-group-item";

            // Getting innerHtml object, adding customer name to it.
            // <li class="something">[creating this object]</li>
            var id = document.createTextNode(`${reimbursement.id}`);
            var tApproved = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
            var tResolved = document.createTextNode(`${reimbursement.resolved.year}-${reimbursement.resolved.monthValue}-${reimbursement.resolved.dayOfMonth}, ${reimbursement.resolved.hour}:${reimbursement.resolved.minute}:${reimbursement.resolved.second}`);
            var approver = document.createTextNode("-");
            var amount = document.createTextNode(`${reimbursement.amount}`);
            var description = document.createTextNode(`${reimbursement.description}`);
            var status = document.createTextNode(`${reimbursement.status.status}`);
            var type = document.createTextNode(`${reimbursement.type.type}`);
            //Append innerHTML to the customer node
            tabledata1.appendChild(id);
            tabledata2.appendChild(tApproved);
            tabledata3.appendChild(tResolved);
            tabledata4.appendChild(approver);
            tabledata5.appendChild(amount);
            tabledata6.appendChild(description);
            tabledata7.appendChild(status);
            tabledata8.appendChild(type);

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
            
            customerList.appendChild(tablerow);
        });
    }
}
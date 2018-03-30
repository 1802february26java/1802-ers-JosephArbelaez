window.onload = () =>{

    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");

    document.getElementById("pending").addEventListener("click", getAllPendingReimbursements);
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
      xhr.open("POST",`reimbursements.do?fetch=pending`);
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
            // Add class for styling <li class = "something">
            //customerNode.className = "list-group-item";

            // Getting innerHtml object, adding customer name to it.
            // <li class="something">[creating this object]</li>
            var text1 = document.createTextNode(`${reimbursement.id}`);
            var text2 = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
            var text3 = document.createTextNode(`${reimbursement.amount}`);
            var text4 = document.createTextNode(`${reimbursement.description}`);
            var text5 = document.createTextNode(`${reimbursement.approver.lastName}, ${reimbursement.approver.firstName}`);
            var text6 = document.createTextNode(`${reimbursement.type.type}`);
            //Append innerHTML to the customer node
            tabledata1.appendChild(text1);
            tabledata2.appendChild(text2);
            tabledata3.appendChild(text3);
            tabledata4.appendChild(text4);
            tabledata5.appendChild(text5);
            tabledata6.appendChild(text6);

            //Finally, we append the new CustomerNode to the CustomerList
            //<ul id="CustomerList">
            //<li class ="something">
            tablerow.appendChild(tabledata1);
            tablerow.appendChild(tabledata2);
            tablerow.appendChild(tabledata3);
            tablerow.appendChild(tabledata4);
            tablerow.appendChild(tabledata5);
            tablerow.appendChild(tabledata6);
            customerList.appendChild(tablerow);
        });
    }
}
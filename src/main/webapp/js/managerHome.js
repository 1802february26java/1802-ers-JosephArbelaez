window.onload = () =>{

    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");
}
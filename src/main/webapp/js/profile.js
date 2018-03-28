window.onload = () =>{
    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");
    document.getElementById("firstName").innerHTML = "First Name: " + sessionStorage.getItem("employeeFirstName");
    document.getElementById("lastName").innerHTML = "Last Name: " + sessionStorage.getItem("employeeLastName");
    document.getElementById("userName").innerHTML = "Username: " + sessionStorage.getItem("employeeUsername");
    document.getElementById("email").innerHTML = "Email: " + sessionStorage.getItem("email");
}

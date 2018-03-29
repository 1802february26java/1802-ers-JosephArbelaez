window.onload = () =>{
    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeFirstName") + " " +sessionStorage.getItem("employeeLastName");
    document.getElementById("firstName").innerHTML = "First Name: " + sessionStorage.getItem("employeeFirstName");
    document.getElementById("lastName").innerHTML = "Last Name: " + sessionStorage.getItem("employeeLastName");
    document.getElementById("userName").innerHTML = "Username: " + sessionStorage.getItem("employeeUsername");
    document.getElementById("email").innerHTML = "Email: " + sessionStorage.getItem("email");

document.getElementById("submit").addEventListener("click", () => {
    let firstname = document.getElementById("firstNameText").value;
    let lastname = document.getElementById("lastNameText").value;
    let username = document.getElementById("usernameText").value;
    let email = document.getElementById("emailText").value;

    // Ajax
    let xhr =  new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.onreadystatechange === XMLHttpRequest.DONE && xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            submit(data);
        }
    };

    xhr.open("POST", `profileUpdate.do?firstname=${firstname}&lastname=${lastname}&username=${username}&email=${email}`)
    xhr.send();

    setTimeout(() =>{ window.location.replace("profile.html");}, 2000);

})
}

function submit(data) {
    if(data.message === "REGISTRATION SUCCESSFUL") {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Profile Change Successfule</span>';
        setTimeout(() =>{ window.location.replace("profile.html");}, 2000);
    }
    //Something went wrong
    else {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-success label-center">Something went wrong.</span>';
    }
}
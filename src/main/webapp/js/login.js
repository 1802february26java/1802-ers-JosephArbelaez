window.onload = () => {
    if (window.location.pathname !=="/ERS/login.do") {
        window.location.replace("login.do");
    }

    document.getElementById("login").addEventListener("click", () => {
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
    
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                console.log(data);
                login(data);
            }
        };

        xhr.open("POST", `login.do?username=$username=${username}&password=${password}`);
        xhr.send();
    });
}

function login(data) {
    if(data.message) {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Username and Password pair is incorrect.</span>';
    } else {
        sessionStorage.setItem("id", data.id);
        sessionStorage.setItem("firstName", data.firstname);
        sessionStorage.setItem("lastName", data.lastName);
        sessionStorage.setItem("username", data.username);
        sessionStorage.setItem("password", data.password);
        sessionStorage.setItem("email", data.email);
        sessionStorage.setItem("employeeRole",JSON.stringify(data.employeeRole));
        window.location.replace("home.do");
    }
}
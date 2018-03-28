window.onload = () => {
    if(window.location.pathname !== '/ERS/login.do') {
       window.location.replace('login.do');
    }

    document.getElementById("login").addEventListener("click", () => {
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        //AJAX Logic
        let xhr = new XMLHttpRequest();
        
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                console.log(data);

                login(data);
            }
        };

        xhr.open("POST",`login.do?username=${username}&password=${password}`);

        xhr.send();
    });
}

function login(data) {
    if(data.message) {
        document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
    }
    else {
        sessionStorage.setItem("employeeId", data.id);
        sessionStorage.setItem("employeeFirstName", data.firstName);
        sessionStorage.setItem("employeeLastName", data.lastName);
        sessionStorage.setItem("employeeUsername", data.username);
        sessionStorage.setItem("employeePassword", data.password);
        sessionStorage.setItem("email",data.email);
        sessionStorage.setItem("employeeRole",JSON.stringify(data.employeeRole));

        let temp = sessionStorage.getItem('employeeRole');
        let viewEmployeeRole = JSON.parse(temp);
        let role = viewEmployeeRole.id
        
        if(role === 1){
        window.location.replace("home.do");
        }
        if(role === 2){
       	window.location.replace("managerHome.do");
        }
    }
}
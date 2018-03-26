window.onload = () => {
    if (window.location.pathname !=='/ERS/login.do') {
        window.location.replace('login.do');
    }

    document.getElementById("login").addEventListener("click", () => {
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
    
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () =>{
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                console.log(data);
            }
        };

        xhr.open("POST", `login.do?username=${username}&password=${password}`);
        xhr.send();
    });
}
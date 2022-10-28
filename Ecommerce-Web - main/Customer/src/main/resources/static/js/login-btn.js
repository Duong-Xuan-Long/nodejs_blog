const emailEl = document.getElementById("name");
const passwordEl = document.getElementById("password");
document
    .getElementById("login-button").addEventListener("click",async function(e){
        e.preventDefault();
        try {
            let email = emailEl.value;
            let password = passwordEl.value;
            let res = await axios.post("/shop/do-login", {
                email,
                password
            })

            window.location.href="/shop";
        } catch (error) {
            alert("Mật khẩu hoặc email không dúng kiểm tra lại");
            console.log(error.response.data);
        }
})
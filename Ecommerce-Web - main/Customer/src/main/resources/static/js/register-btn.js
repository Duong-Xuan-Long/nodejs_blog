const firstNameEl=document.getElementById("firstName");
const lastNameEl=document.getElementById("lastName");
const emailEl = document.getElementById("userName");
const passwordEl = document.getElementById("password");
const  repeatPasswordEl=document.getElementById("repeatPassword");
document
    .getElementById("btn-register").addEventListener("click",async function(e){
    e.preventDefault();
    try {
        let firstName=firstNameEl.value;
        let lastName=lastNameEl.value;
        let email = emailEl.value;
        let password = passwordEl.value;
        let repeatPassword=repeatPasswordEl.value;
        if(password!=repeatPassword){
            alert("Mật khẩu không giống nhau nhập lại");
        }else{
            let res = await axios.post("/shop/do-register", {
                firstName,
                lastName,
                email,
                password,
                repeatPassword
            })

            window.location.href="/shop/login";
            alert("Bạn đã đăng kí thành công bấm vào email để kích hoạt tài khoản");
        }

    } catch (error) {
        console.log(error.response.data);
    }
})
export default function validateToken() {
    const user = JSON.parse(localStorage.getItem('user'));
    if(user != null ){
        const timestamp = user.expiry - 3600
        const expiry = new Date(timestamp * 1000)
        if(new Date() > expiry) {
            localStorage.removeItem("user");
            //TODO do this in more react way
            window.location.reload(false)
        }
    }

}

import React from "react";

class Logout extends React.Component {

    render() {
        localStorage.removeItem("user");
        window.location.reload(false)
        return(<div></div>);
    }
}

export default Logout;

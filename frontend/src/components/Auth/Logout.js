import React from "react";

class Logout extends React.Component {

    render() {
        localStorage.removeItem("user");
        //TODO do this in more react way
        window.location.reload(false)
        return(<div></div>);
    }
}

export default Logout;

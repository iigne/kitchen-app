import React from "react";

class Logout extends React.Component {

    render() {
        this.props.handleLogout();
        return (<></>);
    }
}

export default Logout;

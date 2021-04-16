import React from 'react';
import ReactDOM from 'react-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import App from './App/App';
import reportWebVitals from './reportWebVitals';
import {positions, Provider as AlertProvider} from 'react-alert'
import {Alert} from "react-bootstrap";
import {faCheckCircle, faInfoCircle, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const options = {
    offset: "20px",
    timeout: 5000,
    position: positions.TOP_CENTER,
    containerStyle: {
        zIndex: 9999,
        position: "fixed"
    }
}

const alertStyle = new Map ([
    ['info', {variant: "info", icon: faInfoCircle, color: "#1530b1"}],
    ['success', {variant: "success", icon: faCheckCircle, color: "#66B447"}],
    ['error', {variant: "danger", icon: faTimesCircle, color: "#E9692C"}]
])

const CustomAlertTemplate = ({style, options, message, close}) => {
    const alert = alertStyle.get(options.type)
    return(
        <Alert style={style} variant={alert.variant} onClose={close} dismissible>
           <FontAwesomeIcon icon={alert.icon} style={{color: alert.color}}/>
            {" "}
            {message}
        </Alert>
    )
}

ReactDOM.render(
  <React.StrictMode>
      <AlertProvider template={CustomAlertTemplate} {...options}>
          <App />
      </AlertProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

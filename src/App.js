import logo from './logo.svg';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCarrot} from "@fortawesome/free-solid-svg-icons";

import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <FontAwesomeIcon icon={faCarrot}></FontAwesomeIcon>
        <p>Hello from kitchen app!</p>
      </header>
    </div>
  );
}

export default App;

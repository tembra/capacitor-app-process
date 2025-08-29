import { AppProcess } from 'capacitor-app-process';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    AppProcess.echo({ value: inputValue })
}

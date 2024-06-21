import { ScreenEvents } from 'screen-events';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    ScreenEvents.echo({ value: inputValue })
}

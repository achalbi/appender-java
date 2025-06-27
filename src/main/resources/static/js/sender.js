document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('sender-form'); // Assuming the form has an id 'sender-form'
    const inputField = document.getElementById('message-input'); // Assuming the input has an id 'message-input'
    const responseDiv = document.getElementById('response');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const inputValue = inputField.value;
        const postData = {
            input: inputValue
        };

        try {
            const response = await fetch('/append', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(postData)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const responseText = await response.text();
            responseDiv.textContent = 'Sent: ' + inputValue + '\nResponse: ' + responseText;

        } catch (error) {
            responseDiv.textContent = 'Error: ' + error.message;
            console.error('Error sending message:', error);
        }
    });
});
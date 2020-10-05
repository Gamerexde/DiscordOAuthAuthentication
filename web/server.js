const express = require('express')
const app = express()

const port = process.env.PORT || 4000

app.listen(port, () => {
    console.log("Backend server has started! at http://localhost:" + port)
})

app.get('/', (req, res) => {
    res.send("Are you lost?")
})
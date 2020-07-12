
const { Client, logger } = require('camunda-external-task-client-js');
const open = require('open');

// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events
//  - 'asyncResponseTimeout': long polling timeout (then a new request will be issued)
const config = { baseUrl: 'http://localhost:8080/engine-rest', use: logger, asyncResponseTimeout: 10000 };

// create a Client instance with custom configuration
const client = new Client(config);

client.subscribe('processEvent1', async function({ task, taskService }) {
  // Put your business logic here

  // Get a process variable
  //const amount = task.variables.get('amount');

  console.log(`processEvent2 just happened`);
  
  // Complete the task
  await taskService.complete(task);
})


client.subscribe('processEvent2', async function({ task, taskService }) {

  console.log(`processEvent2 just happened`);
  
  // Complete the task
  await taskService.complete(task);
})

client.subscribe('processEvent3', async function({ task, taskService }) {

  console.log(`processEvent3 just happened`);
  
  // Complete the task
  await taskService.complete(task);
})

client.subscribe('processEvent4', async function({ task, taskService }) {

  console.log(`processEvent4 just happened`);
  
  // Complete the task
  await taskService.complete(task);
})

;

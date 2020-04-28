$deployments = Invoke-RestMethod -Uri http://localhost:8080/engine-rest/deployment/ -Method "Get" -ContentType "application/json"
foreach ($d in $deployments) {
    if ($d.name.Equals("einarbeiter")) {
        $uri = "http://localhost:8080/engine-rest/deployment/" + $d.id + "?cascade=true"
        Invoke-RestMethod -Uri $uri -Method "Delete" -ContentType "application/json"
    }
}
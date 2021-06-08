# MeSkin-B21-CAP0205
MeSkin will be the first-ever app to help you diagnose your skin problem only with a photo accurately and efficiently. MeSkin will provide you with the latest information about health in Indonesia. MeSkin also provides an ease to access information about diagnosed disease. MeSkin is able to generate a document based on the prediction of the application.

In this project we use compute engine from Google Cloud Platform, with specification:
Name : flask-server
region : us-central1
Zone : us-central1-a
Machine type : e2-medium(vCPUs, 4GB Memory)
CPU platform : Intel Haswell
Network Interfaces : default
Firewalls : allow HTTP traffic
Bootdisk : ubuntu-1804-bionic-v20210514
Size : 50GB
Type : Balanced persistent disk.

We also use firebase storage to save the picture from the aplication.
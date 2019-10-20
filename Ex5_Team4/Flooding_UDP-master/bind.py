import socket
import sys
import time

my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
my_socket.bind(('',5004))

print (" I am pi 192.168.210.199")
message , address = my_socket.recvfrom(1024)
print "message " + str(message) + "from : " + str(address)
address_sender = address
IPs = str(message) + "**" + " " + str(address)	
print ("Ips traversed: " + str(IPs))
time.sleep(5)


my_socket.close()
my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
my_socket.bind(('192.168.210.199',5004))
for i in range(3):
	my_socket.sendto(IPs, ('<broadcast>' ,5004))


my_socket.close()
time.sleep(1)
my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
my_socket.bind(('',5004))


my_socket.settimeout(3)


while 1:

        try:
                message_re , address_re = my_socket.recvfrom(1024)
                if (address_sender[0] == address_re[0]):
			print ("Inside if cond") 
			for i in range(3):
				my_socket.sendto(IPs, address_sender)
		else:
			IPs = str(message) + "**" + " " + str(address)	
			print ("Else")	
			for i in range(3):
				my_socket.sendto(IPs, address_sender)	

        except socket.timeout:
       		print ("Exception")         
		for i in range(4):
			my_socket.sendto(IPs, address_sender)






print ("Exiting")


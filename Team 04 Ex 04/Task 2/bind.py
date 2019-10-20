import socket
import sys
import time
import pickle
my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
my_socket.bind(('',5004))

print (" I am pi 192.168.210.152")
message , address = my_socket.recvfrom(1024)
print "message " + str(message) + "from : " + str(address)
address_sender = address
IPs = message 
Packet_IP = [x.strip() for x in message.split('**')]

if '192.168.210.152' in Packet_IP[3]:
        IPs = IPs.replace('RREQ', 'RREP') 
	print ("Reached Destination")
	print (IPs)
	for i in range(4):
                my_socket.sendto(IPs, address_sender)
 
else:
	IPs = str(message) + "**" +  "192.168.210.152"
	print ("Ips traversed: " + str(IPs))

	my_socket.close()
	my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
	my_socket.bind(('192.168.210.152',5004))
	for i in range(3):
		my_socket.sendto(IPs, ('<broadcast>' ,5004))



	my_socket.close()
	my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
	my_socket.bind(('',5004))


	my_socket.settimeout(10)


	for i in range(3):
        	try:
                	message_re , address_re = my_socket.recvfrom(1024)
                	if (address_sender[0] == address_re[0]):
				print ("Inside if cond") 
				for i in range(3):
					my_socket.sendto(message_re, address_sender)
			else:
				print ("Else")	
				for i in range(3):
					my_socket.sendto(message_re, address_sender)	

        	except socket.timeout:
       			print ("Exception")         
			for i in range(4):
				my_socket.sendto(IPs, address_sender)


while 1:
        #code for sending packet from source to destination
        message_re , address_re = my_socket.recvfrom(1024)
        if 'RREP' in message_re:
                continue
        else:
                packet = pickle.loads(message_re)
                # Check if I am the destination
                if not '192.168.210.152' in Packet_IP[1]:
                        del packet[2]
                        toAddress = packet[2]
                        MESSAGE = pickle.dumps(packet)
			print str(packet) 
			print toAddress
                        if len(packet) != 3:
				for i in range(4):
                                	my_socket.sendto(MESSAGE,(toAddress,5004))
			else:
				toAddress = packet[1]	
				for i in range(4):
                                        my_socket.sendto(MESSAGE,(toAddress,5004))
                else:
                        print ("Reached Destination!!")
                        print ("Message " + packet(2))




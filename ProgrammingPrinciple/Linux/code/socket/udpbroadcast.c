#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<unistd.h>

void main()
{
    int socketfd = -1;
    const int opt = 1;
    int nb = 0;
    int addr_len;
    struct sockaddr_in server_addr;
    char buff[] = {"hello! I am here!"};
    int flag = 10;
    if( (socketfd = socket(PF_INET, SOCK_DGRAM, 0)) < 0 )
    {
        perror("socker error");
        exit(-1);
    }
    nb = setsockopt(socketfd, SOL_SOCKET, SO_BROADCAST, &opt, sizeof(opt));
    if( -1 == nb )
    {
        perror("setsockopt error");
        exit(-1);
    }
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_BROADCAST);
    server_addr.sin_port = htons(8700);
    addr_len = sizeof(server_addr);
    while(flag > 0)
    {
        sleep(1);
        if( sendto(socketfd, buff, strlen(buff), 0, (struct sockaddr*)&server_addr, addr_len) < 0 )
        {
            perror("sendto error");
            exit(-1);
        }
        else
        {
            printf("ok \n");
        }
        flag--;
    }
    close(socketfd);
}
clc
clear all
rng default  % For reproducibility
rng(1); % For reproducibility
M=csvread('tfIdf.csv');
size(M)

figure(1);
plot(M(:,1),'k*','MarkerSize',5);
hold on
plot(M(:,2),'k+','MarkerSize',5);
title 'TfIdf values for US involved in Workgroup and Metada with Higher Weights for WG';
xlabel 'Documents';
ylabel 'Tf-IDf Values for Workgroup and Resource terms';

figure(4)
plot(M(:,1),M(:,2),'*','MarkerSize',5);

[idx,C] = kmeans(M,2);

x1 = min(M(:,1)):0.01:max(M(:,1));
x2 = min(M(:,2)):0.01:max(M(:,2));
[x1G,x2G] = meshgrid(x1,x2);
XGrid = [x1G(:),x2G(:)]; % Defines a fine grid on the plot

idx2Region = kmeans(XGrid,2,'MaxIter',20,'Start',C);
figure(2);
gscatter(XGrid(:,1),XGrid(:,2),idx2Region,...
[0,0.75,0.75;0.75,0,0.75;0.75,0.75,0],'..');
hold on;
plot(M(:,1),M(:,2),'k*','MarkerSize',5);


figure(3);
plot(M(idx==1,1),M(idx==1,2),'r.','MarkerSize',20)
hold on
plot(M(idx==2,1),M(idx==2,2),'b.','MarkerSize',20)
plot(C(:,1),C(:,2),'kx',...
     'MarkerSize',15,'LineWidth',3)
legend('Cluster 1(workgroup)','Cluster 2(metadata)','Centroids','Location','NW')
title 'Clustering User Stories based on Workgroup and Metada Resources with Higher Weights for WG'
xlabel 'Tf-IDf Values for term Workgroup';
ylabel 'Tf-IDf Values for term Metadata';


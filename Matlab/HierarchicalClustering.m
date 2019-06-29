format short g
M=csvread('CosSim.csv');
size(M)
% Y=pdist(M);
% N=squareform(Y);
linkage_group_distance = linkage(M,'complete')
dendrogram(linkage_group_distance)



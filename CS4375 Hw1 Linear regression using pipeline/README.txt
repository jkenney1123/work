part1.py
You need to have the following libraries
pandas
numpy
math
matplotlib.pyplot

for the code to run correctly

Description of the code:

initialize learning rate, max_iter, train_test_ratio
read data into dataframe called df
create numpy array of 3 attributes with 90% or 80% of the data called dtr
create numpy array of output of size (90% or 80% of df's datapoints,1) called ytr
create weight numpy array of size (1,3) with random values called W
prints initial weight array

#training
for loops over the max number of iterations specified
	calcs the Mean squared error
	stores the MSE in MSE_log so you can pot the iterations vs MSE
	for loops over the number of attributes
		calcs the derivative of MSE
		updates weight for that attribute by taking the old weight  - learning rate * derivative of MSE
calcs the final MSE of the training set  and prints it out
prints the final updated weight array
#test
create numpy array of 3 attributes with 10% or 20% of the data called dte
create numpy array of output of size (10% or 20% of df's datapoints,1) called yte
stores predicted y values by taking the dot product of the testing set and the transpose of the weight array after training
calculates the MSE of the testing data set from the weights calculated from the traing data set
prints the MSE of the testing data set

plots iteration vs MSE during training
plots y actual vs y predicted on the testing dataset
plots the dataframe attribute X2 vs actual y 
plots the dataframe attribute X3 vs actual y
plots the dataframe attribute X4 vs actual y 



part2.py
You need to have the following libraries
pandas
numpy
math
sklearn.linear_model
sklearn.pipeline
sklearn.preprocessing
matplotlib.pyplot
sklearn.metrics

for the code to run correctly

Description of the code:

initialize learningRate, max_iterations, train_test_ratio
read data into dataframe called df
create numpy array of 3 attributes with 90% or 80% of the data called dtr
create numpy array of output of size (90% or 80% of df's datapoints,) called ytr

Then use make_pipline with a standardscaler and SGDRegressor with paramerters of max_iterations and learningRate called reg
fit the training data set of attributes to the actual y set
use reg to predict y from the test data set
get the R^2 value between testing data and testing output
get the MSE between the y actual and y predicted 

plot the y actual versus the y predicted












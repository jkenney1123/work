You need to have the following libraries
pandas
numpy
sklearn.model_selection
sklearn.metrics
plotly.graph_objects

to specify the parameters for the desired classification read below for location and instructions:


at the bottom of the code directly below 3 lines filled with lots of #'s
you can change the parameters used on the dataset
in order of the lines code
to declare the activation function you want to use uncommment the function you want to use and comment the other lines you don't want to use
next you can specify the number of iterations the training set goes through by changeing
what _max__iterations ='s to
likewise you specify the train size to a different percentage , but for my trial I kept the ratio at .8/.2
for changing learning rate you can just change what the learningrate is equal to
and  lastly to specify the number of neuron's hidden in the hidden layer change what neurons_hidden equals to


fyi
I have found that the parameters can give some strange outputs with the same parameters used so if not desired result found run it agian a few times to see if it works then
alse if too big of a learning rate is used and depending on the number of iterations specified np.exp() will give nan values  try a smaller learning rate or smaller number of iterations

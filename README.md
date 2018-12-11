# MultiGraph
Software component acting as a simulator and aiming to help in the deployment of novel Attack Graph (AG) models. 
It is intended to help comparing these novel approaches with already existing designs and implementations. 
It has also as an objective to determine those aspects of existing models that have not been completely defined or specified by their authors and thus may need some completion before being used in lab or real attack scenarios.  

### Features
* AG import from MulVAL XML files (w.r.t. specific rules file, to be uploaded here)
* Manual configuration of the AG
* Optimization problem solutions:
	* Poolsappasit et al. - SOOP & MOOP
	* Wang L. et al.
* Interactive graph
* Export CSV lists of optimal recommendations
* Comparisons of different methods and strategies - TODO

### GUI 
![GUI](/src/es/um/multigraph/resources/images/MultiGraphScreen2.png)

### Bayesian AG with 3 vulnerabilities 
![Attack Graph](/src/es/um/multigraph/resources/images/MultiGraphScreen3.png)


## Input XML file generated by MulVAL 
AG with one vulnerability
```

```

## Output of prescriptions
(Poolsappasit et al. case)
```
cmId,targetId,type
pn15,n15,SI_02: FLAW REMEDIATION
```

(L. Wang et al. case)
```
nodeIds
!n4 | (!n3 & !n8) | (!n3 & !n9) | (!n5 & !n8) | (!n5 & !n9)
```


## References   
* M. Zago, Modeling Cyber-Threats: Adopting Bayes' principles in the Attack Graph theory, 2015  
* Poolsappasit, N., Dewri, R., and Ray, I. Dynamic security risk management using bayesian attack graphs. IEEE Transactions on Dependable and Secure Computing 9, 1 (Jan 2012), 61–74.  
* Wang L., Albanese M., Jajodia S., Network Hardening An Automated Approach to Improving Network Security, Springer International Publishing, Cham 2014, pp. 15-22
* H. M. J. Almohri, L. T. Watson, D. Yao and X. Ou, "Security Optimization of Dynamic Networks with Probabilistic Graph Modeling and Linear Programming," in IEEE Trans. on Dep. & Secure Computing, vol. 13, no. 4, pp. 474-487, 1 July-Aug. 2016.


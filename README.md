# MultiGraph
Software component acting as a simulator and aiming to help in the deployment of novel Attack Graph (AG) models. 
It is intended to help comparing these novel approaches with already existing designs and implementations. 
It has also as an objective to determine those aspects of existing models that have not been completely defined or specified by their authors and thus may need some completion before being used in lab or real attack scenarios.  

### Features
* AG import from MulVAL XML files (w.r.t. specific rules file, to be uploaded here)
* Manual configuration of the AG
* Optimization problem solutions:
	* Poolsappasit et al. - SOOP & MOOP
	* Wang L. et al. - TODO
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
<attack_graph>
  <arcs>
    <arc>
      <src>2</src>
      <dst>3</dst>
    </arc>
    <arc>
      <src>2</src>
      <dst>4</dst>
    </arc>
    <arc>
      <src>2</src>
      <dst>5</dst>
    </arc>
    <arc>
      <src>2</src>
      <dst>6</dst>
    </arc>
    <arc>
      <src>1</src>
      <dst>2</dst>
    </arc>
  </arcs>
  <vertices>
    <vertex>
      <id>1</id>
      <fact>execCode(ftpServer,root)</fact>
      <metric>0</metric>
      <type>OR</type>
    </vertex>
    <vertex>
      <id>2</id>
      <fact>RULE 0 (Vulnerable program is running on remotely accessible host)</fact>
      <metric>0</metric>
      <type>AND</type>
    </vertex>
    <vertex>
      <id>3</id>
      <fact>hacl(internet,ftpServer,tcp,20)</fact>
      <metric>1</metric>
      <type>LEAF</type>
    </vertex>
    <vertex>
      <id>4</id>
      <fact>attackerLocated(internet)</fact>
      <metric>1</metric>
      <type>LEAF</type>
    </vertex>
    <vertex>
      <id>5</id>
      <fact>progRunning('Matu FTP',ftpServer)</fact>
      <metric>1</metric>
      <type>LEAF</type>
    </vertex>
    <vertex>
      <id>6</id>
      <fact>vulExists(ftpServer,'CVE-9999',40,'Matu FTP')</fact>
      <metric>1</metric>
      <type>LEAF</type>
    </vertex>
  </vertices>
</attack_graph>
```

## CSV output of prescriptions
(Poolsappasit et al. case)
```
cmId,targetId,type
pn15,n15,SI_02: FLAW REMEDIATION
```


## References   
* M. Zago, Modeling Cyber-Threats: Adopting Bayes' principles in the Attack Graph theory, 2015  
* Poolsappasit, N., Dewri, R., and Ray, I. Dynamic security risk management using bayesian attack graphs. IEEE Transactions on Dependable and Secure Computing 9, 1 (Jan 2012), 61–74.  
* Wang L., Albanese M., Jajodia S., Network Hardening An Automated Approach to Improving Network Security, Springer International Publishing, Cham 2014, pp. 15-22

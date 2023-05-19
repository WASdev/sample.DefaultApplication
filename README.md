# Modernizing the traditional WebSphere DefaultApplication.ear

The article in this README discusses the original runtime modernization of the DefaultApplication from traditional WebSphere Application Server to Liberty. Since that initial transformaton, we have taken it further to [Jakarta EE 9.1](./jakarta9/README.md) and [Jakarta EE 10](./jakarta10/README.md).

## Runtime Modernization to Liberty  

Sometimes applications last longer than we expect.  DefaultApplication.ear has been shipping as a WebSphere Application Server sample program for many releases. Classes in the application date back as far as 1997. We have seen [Transformation Advisor](https://ibm.biz/cloudta) system scans of WebSphere cells where DefaultApplication is the only complex application in the cell. That’s pretty embarrassing, so it was time for us to do some application modernization ourselves. This article shows the process we went through to update DefaultApplication so that it uses more modern programming models and so that it can run on Liberty or traditional WebSphere.

The updated DefaultApplication ships with traditional WebSphere Application Server V9.0.0.11. If you install a new profile and install the sample applications, you will see the new version of DefaultApplication. If you already have DefaultApplication installed and apply the fix pack, it will not be automatically updated. You can update the application with the version shipped in the `installableApps` folder.

There have been several phases to this modernization. Each step builds on the previous. The first step had the most code changes to convert entity EJB beans to JPA. The Jakarta 9.1 step involved changing to the Jakarta namespace by renaming the Java packages from javax to jakarta. And the final step up to Jakarta 10 had an API incompatibility that needed to be changed. 

1. [Runtime modernization to Liberty](./modernized/README.md)
2. [Modernize to Jakarta 9.1](./jakarta9/README.md)
3. [Modernize to Jakarta 10](./jakarta10/README.md)



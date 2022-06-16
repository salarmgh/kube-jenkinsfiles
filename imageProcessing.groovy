pipeline {
  agent {
    kubernetes {
      yaml """
kind: Pod
spec:
  containers:
  - name: dind
    image: docker:stable-dind
    imagePullPolicy: IfNotPresent
    securityContext:
      privileged: true
"""
  }
}

  stages{
    stage('Build') {
       steps {
         script{
          sh "echo build"
        }
      }
    }
    stage('Test'){
      steps{
        script{
          sh "echo text"
        }
      }
    }
    stage('Deploy') {
      steps {
        script{
          sh "echo deploy"
        }
      }
    }
  }
}

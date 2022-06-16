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

  stages {
    stage('Build') {
      steps {
        script {
          checkout([
            $class: "GitSCM",
            branches: [[name: "*/main"]],
            userRemoteConfigs: [[url: "https://github.com/salarmgh/kube-app.git"]]
          ])
          container('dind') {
            script {
              sh """
              docker build -t test -f django/docker/Dockerfile .
              """
            }
          }
        }
      }
    }
    stage('Test') {
      steps {
        script {
          container('dind') {
            script {
              sh """
                echo 'Test ...'
              """
            }
          }
        }
      }
    }
    stage('Deploy') {
      steps {
        script {
          container('dind') {
            script {
              sh """
                echo 'Deploy ...'
              """
            }
          }
        }
      }
    }
  }
}

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
          container('dind') {
            script {
              sh """
                echo 'Building ...'
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
                echo 'Testing ...'
              """
            }
          }
        }
      }
    }
    stage('Push') {
      steps {
        script {
          container('dind') {
            script {
              sh """
                echo 'Pushing ...'
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
                echo 'Deploying ...'
              """
            }
          }
        }
      }
    }
  }
}

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
                echo 'Build ...'
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

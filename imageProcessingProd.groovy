pipeline {
  agent {
    kubernetes {
      yaml """
kind: Pod
spec:
  serviceAccount: deployer
  containers:
  - name: dind
    image: docker:stable-dind
    imagePullPolicy: IfNotPresent
    command:
    - dockerd-entrypoint.sh
    - '--insecure-registry=registry.anisa.lab'
    - '--registry-mirror=http://registry.anisa.lab'
    securityContext:
      privileged: true
  - name: deployer
    image: registry.anisa.lab/deployer
    imagePullPolicy: IfNotPresent
    tty: "true"
    command:
    - /bin/cat
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
                echo 'docker build -t registry.anisa.lab/back -f ./django/docker/Dockerfile ./django'
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
                echo 'docker run registry.anisa.lab/back python manage.py test'
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
                echo 'docker push registry.anisa.lab/back'
              """
            }
          }
        }
      }
    }
    stage('Deploy') {
      steps {
        script {
          container('deployer') {
            script {
              sh """
                helm upgrade --install -n app image-processing ./django/kubernetes/helm/image-processing
              """
            }
          }
        }
      }
    }
  }
}

pipeline {
    agent any

    triggers {
        pollSCM('*/5 * * * *') // Poll Git SCM every 5 minutes for updates
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                // Deploy to the running Web Server container using Ansible
                sh 'ansible-playbook -i hosts.ini playbook.yml'
            }
        }
    }

    post {
        failure {
            emailext (
                subject: "BUILD FAILED: Job '${env.JOB_NAME}' [Build #${env.BUILD_NUMBER}]",
                body: """BUILD FAILED: Job '${env.JOB_NAME}' [Build #${env.BUILD_NUMBER}]
                       
                       Check console output at: ${env.BUILD_URL}
                       
                       Failed commit authors/culprits will find the logs on Jenkins.""",
                recipientProviders: [
                    [$class: 'CulpritsRecipientProvider'],
                    [$class: 'DevelopersRecipientProvider']
                ],
                cc: 'srengty@gmail.com'
            )
        }
    }
}

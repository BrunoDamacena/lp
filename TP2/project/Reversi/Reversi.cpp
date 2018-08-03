#include "Reversi.h"
#include "ui_reversi.h"
#include <QDebug>
#include <QMessageBox>

Reversi::Reversi(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::Reversi) {
    ui->setupUi(this);

    //starting user count
    m_count[0] = 0;
    m_count[1] = 0;

    /* matriciando o grid do jogo */
    for(int row = 0; row < 8; row++) {
        for(int col = 0; col < 8; col++) {
            Cell* cell = this->findChild<Cell*>(
                QString("cell%1%2").arg(row).arg(col)
            );
            Q_ASSERT(cell != 0);
            cell->setRow(row);
            cell->setCol(col);
            QObject::connect(cell, SIGNAL(clicked(bool)), this, SLOT(play()));
            QObject::connect(cell, SIGNAL(playerChanged(int,int)), this, SLOT(updateStatus(int, int)));
            m_cells[row][col] = cell;
        }
    }

    /* iniciando jogadores do grid */
    m_cells[3][3]->setPlayer(1);
    m_cells[3][4]->setPlayer(2);
    m_cells[4][3]->setPlayer(2);
    m_cells[4][4]->setPlayer(1);

    QObject::connect(ui->actionSair, SIGNAL(triggered(bool)), qApp, SLOT(quit()));
    QObject::connect(ui->actionNovo, SIGNAL(triggered(bool)), this, SLOT(reset()));
    QObject::connect(ui->actionSobre, SIGNAL(triggered(bool)), this, SLOT(showAbout()));

    //set window size fixed
    this->adjustSize();
    this->setFixedSize(this->size());
}

Reversi::~Reversi() {
    delete ui;
}

void Reversi::play() {
    Cell* cell = qobject_cast<Cell*>(QObject::sender());
    Q_ASSERT(cell != 0);
    if(cell->player()==0){
        if(verificaLados(cell)){
            switch (player) {
                case 1:
                    player = 2;
                    break;

                case 2:
                    player = 1;
                    break;

                default:
                    break;
            }
        }
        else{
           qDebug()  <<"Não permitido";
        }
    }
    qDebug() << "Play on ("
             << cell->row() << ", "
             << cell->col() << ")";
    if(!fimdejogo()){
        int rebeliao=m_count[0];
        int imperio=m_count[1];
        if(rebeliao>imperio){
            QMessageBox::warning(this, tr("Fim de jogo"),
                                 tr("Parabens!!!\nvitoria da Rebelião."));
        }
        else if(rebeliao<imperio){
            QMessageBox::warning(this, tr("Fim de jogo"), tr("Parabens!!!\n"
                                                                     " vitoria do Império."));
        }
        else if(rebeliao==imperio){
            QMessageBox::warning(this, tr("Fim de jogo"), tr("Empate!!!\n"
                                                                     " Os jogadores possuem os mesmos pontos."));
        }

        reset();
    }
}

void Reversi:: reset() {
    for(int row = 0; row < 8; row++) {
        for(int col = 0; col < 8; col++) {
            m_cells[row][col]->setPlayer(0);
        }
    }
    m_cells[3][3]->setPlayer(1);
    m_cells[3][4]->setPlayer(2);
    m_cells[4][3]->setPlayer(2);
    m_cells[4][4]->setPlayer(1);
    player = 1;
}

void Reversi:: showAbout() {
    QMessageBox::information(this,
        tr("Sobre"),
        tr("Reversi\n\nBruno Nascimento Damacena - damacena.bruno@gmail.com\n\nCódigo-fonte disponível em https://http://github.com/brunodamacena/reversi"));
}

bool Reversi::horizontalPos(int coluna,int linha){
    if(coluna<7){
            if(m_cells[linha][coluna+1]->player()==player||
               m_cells[linha][coluna+1]->player()==0){
                return false;
            }
        for(int j=coluna+2;j<8;j++){
            if(m_cells[linha][j]->player()==player){
                for(int i=coluna;i<j;i++){
                    m_cells[linha][i]->setPlayer(player);
                }
                return true;
            }
            if(m_cells[linha][j]->player()==0){
                return false;
            }
        }
    }
    return false;
}
bool Reversi::horizontalNeg(int coluna,int linha){
    if(coluna>0){
        if(m_cells[linha][coluna-1]->player()==player||
            m_cells[linha][coluna-1]->player()==0){
              return false;
         }
        for(int j=coluna-2;j>=0;j--){
            if(m_cells[linha][j]->player()==player){
                for(int i=coluna;i>j;i--){
                    m_cells[linha][i]->setPlayer(player);
                }
                return true;
            }
            if(m_cells[linha][j]->player()==0){
                return false;
            }
        }
     }
     return false;
}

bool Reversi::verticalNeg(int coluna,int linha){
    if(linha>0){
        if(m_cells[linha-1][coluna]->player()==player||
            m_cells[linha-1][coluna]->player()==0){
            return false;
        }
        for(int j=linha-2;j>=0;j--){
            if(m_cells[j][coluna]->player()==player){
                for(int i=linha;i>j;i--){
                    m_cells[i][coluna]->setPlayer(player);
                }
            return true;
            }
            if(m_cells[j][coluna]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::leftDiagNeg(int coluna,int linha){
    if(linha>0&&coluna<7){
            if(m_cells[linha-1][coluna+1]->player()==player||
               m_cells[linha-1][coluna+1]->player()==0){
                return false;
            }
        for(int j=linha-2, k=coluna+2;j>=0&&k<8;j--,k++){
            if(m_cells[j][k]->player()==player){
                for(int i=linha, l=coluna;i>j;i--,l++){
                    m_cells[i][l]->setPlayer(player);
                }
                return true;
            }
            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::leftDiagPos(int coluna,int linha){
    if(linha<7&&coluna<7){
            if(m_cells[linha+1][coluna+1]->player()==player||
               m_cells[linha+1][coluna+1]->player()==0){
                return false;
            }
        for(int j=linha+2, k=coluna+2;j<8&&k<8;j++,k++){
            if(m_cells[j][k]->player()==player){
                for(int i=linha, l=coluna;i<j;i++,l++){
                    m_cells[i][l]->setPlayer(player);
                }
                return true;
            }

            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }

    return false;

}

bool Reversi::rightDiagNeg(int coluna,int linha){
    if(coluna>0&&linha>0){
            if(m_cells[linha-1][coluna-1]->player()==player||
               m_cells[linha-1][coluna-1]->player()==0){
                return false;
            }

        for(int j=linha-2, k=coluna-2;j>=0&&k>=0;j--,k--){
            if(m_cells[j][k]->player()==player){
                for(int i=linha, l=coluna;i>j;i--,l--){
                    m_cells[i][l]->setPlayer(player);
                }
                return true;
            }

            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;

}

bool Reversi::rightDiagPos(int coluna,int linha){
    if(linha<7&&coluna>0){
            if(m_cells[linha+1][coluna-1]->player()==player||
               m_cells[linha+1][coluna-1]->player()==0){
                return false;
            }

        for(int j=linha+2, k=coluna-2;j<8&& k>=0;j++,k--){
            if(m_cells[j][k]->player()==player){
                for(int i=linha, l=coluna;i<j;i++,l--){
                    m_cells[i][l]->setPlayer(player);
                }
                return true;
            }
            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::verificaLados(Cell* cell){
    int coluna=cell->col();
    int linha=cell->row();
    int verifica=0;
    qDebug() << player;

    if(this->verticalNeg(coluna,linha)){
        verifica++;
    }
    if(this->horizontalPos(coluna,linha)){
        verifica++;
    }
    if(this->horizontalNeg(coluna,linha)){
        verifica++;
    }
    if(this->horizontalPos(coluna,linha)){
        verifica++;
    }
    if(this->rightDiagNeg(coluna,linha)){
        verifica++;
    }
    if(this->rightDiagPos(coluna,linha)){
        verifica++;
    }
    if(this->leftDiagNeg(coluna,linha)){
        verifica++;
    }
    if(this->leftDiagPos(coluna,linha)){
        verifica++;
    }
    if(verifica>0){
        return true;
    }
    return false;
}

bool Reversi::verificaFim(Cell* cell){
    int coluna=cell->col();
    int linha=cell->row();
    int verifica=0;

    if(this->verticaln(coluna,linha)){
        verifica++;
    }
    if(this->verticalp(coluna,linha)){
        verifica++;
    }
    if(this->horizontaln(coluna,linha)){
        verifica++;
    }
    if(this->horizontalp(coluna,linha)){
        verifica++;
    }
    if(this->diagdirn(coluna,linha)){
        verifica++;
    }
    if(this->diagdirp(coluna,linha)){
        verifica++;
    }
    if(this->diagesqn(coluna,linha)){
        verifica++;
    }
    if(this->diagesqp(coluna,linha)){
        verifica++;
    }
    if(verifica>0){
        return true;
    }
    return false;
}

bool Reversi::horizontalp(int coluna,int linha){
    if(coluna<7){
            if(m_cells[linha][coluna+1]->player()==player||
               m_cells[linha][coluna+1]->player()==0){
                return false;
            }
        for(int j=coluna+2;j<8;j++){
            if(m_cells[linha][j]->player()==player){
                return true;
            }
            if(m_cells[linha][j]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::horizontaln(int coluna,int linha){
    if(coluna>0){
        if(m_cells[linha][coluna-1]->player()==player||
            m_cells[linha][coluna-1]->player()==0){
              return false;
         }
        for(int j=coluna-2;j>=0;j--){
            if(m_cells[linha][j]->player()==player){
                return true;
            }
            if(m_cells[linha][j]->player()==0){
                return false;
            }
        }
     }
     return false;
}

bool Reversi::verticalp(int coluna,int linha){
    if(linha<7){
        if(m_cells[linha+1][coluna]->player()==player||
           m_cells[linha+1][coluna]->player()==0){
            return false;
        }
    }
    for(int j=linha+2;j<8;j++){
        if(m_cells[j][coluna]->player()==player){
             return true;
        }
        if(m_cells[j][coluna]->player()==0){
            return false;
        }
    }
    return false;
}

bool Reversi::verticaln(int coluna,int linha){
    if(linha>0){
            if(m_cells[linha-1][coluna]->player()==player||
               m_cells[linha-1][coluna]->player()==0){
                return false;
            }
        for(int j=linha-2;j>=0;j--){
            qDebug()  <<"J:" <<j;
            if(m_cells[j][coluna]->player()==player){
                 return true;
            }
            if(m_cells[j][coluna]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::diagesqn(int coluna,int linha){
    if(linha>0&&coluna<7){
            if(m_cells[linha-1][coluna+1]->player()==player||
               m_cells[linha-1][coluna+1]->player()==0){
                return false;
            }
        for(int j=linha-2, k=coluna+2;j>=0&&k<8;j--,k++){
            if(m_cells[j][k]->player()==player){
                return true;
            }
            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::diagesqp(int coluna,int linha){
    if(linha<7&&coluna<7){
            if(m_cells[linha+1][coluna+1]->player()==player||
               m_cells[linha+1][coluna+1]->player()==0){
                return false;
            }
        for(int j=linha+2, k=coluna+2;j<8&&k<8;j++,k++){
            if(m_cells[j][k]->player()==player){
                return true;
            }
            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::diagdirn(int coluna,int linha){
    if(coluna>0&&linha>0){
            if(m_cells[linha-1][coluna-1]->player()==player||
               m_cells[linha-1][coluna-1]->player()==0){
                return false;
            }
        for(int j=linha-2, k=coluna-2;j>=0&&k>=0;j--,k--){
            if(m_cells[j][k]->player()==player){
                return true;
            }
            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::diagdirp(int coluna,int linha){
    if(linha<7&&coluna>0){
            if(m_cells[linha+1][coluna-1]->player()==player||
               m_cells[linha+1][coluna-1]->player()==0){
                return false;
            }
        for(int j=linha+2, k=coluna-2;j<8&& k>=0;j++,k--){
            if(m_cells[j][k]->player()==player){
                return true;
            }
            if(m_cells[j][k]->player()==0){
                return false;
            }
        }
    }
    return false;
}

bool Reversi::fimdejogo(){
    for(int i=0;i<8;i++){
        for(int j=0;j<8;j++){
            if(m_cells[i][j]->player()==0){
                if(this->verificaFim(m_cells[i][j])){
                    return true;
                }
            }

        }
    }
    return false;
}

void Reversi::updateStatus(int oldPlayer, int newPlayer) {
    if(oldPlayer > 0) {
        m_count[oldPlayer - 1]--;
    }
    if(newPlayer > 0) {
        m_count[newPlayer - 1]++;
    }
    qDebug() << "old player: " << oldPlayer;
    qDebug() << "new player: " << newPlayer;
    ui->statusBar->showMessage(QString("Rebelião: %1 vs Império: %2").arg(m_count[0]).arg(m_count[1]));
}


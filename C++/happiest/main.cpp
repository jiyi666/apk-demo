#include <iostream>
#include <vector>
#include <fstream>
#include <random>
#include <algorithm>
#include "happycash.h"

using namespace std;

//const int ARRAY_SIZE = 7;
//const int ARRAY_LIMIT = 100;
const int FREQ1_SIZE = 35; // 前五个数字中可能出现的最大值
const int FREQ2_SIZE = 12; // 后两个数字中可能出现的最大值
static int freq1[FREQ1_SIZE + 1] = {0}; // 初始化前五个数字的频率数组
static int freq2[FREQ2_SIZE + 1] = {0}; // 初始化后两个数字的频率数组
static vector<pair<int, int>> freq1_sort, freq2_sort;

//release
void generateOutputmix(int front_hot_num_select, int back_hot_num_select, const vector<pair<int, int>>& freq_sort1, const vector<pair<int, int>>& freq_sort2) {
    int front_hot_num = front_hot_num_select;
    int front_cold_num = 5 - front_hot_num_select;
    int back_hot_num = back_hot_num_select;
    int back_cold_num = 2 - back_hot_num_select;

    //前5区
    vector<int> selected1;
    random_device rd1;
    mt19937 gen1(rd1());
    uniform_int_distribution<> dis1(0, 14);  // 从前15个元素中选择

    for (int i = 0; i < front_hot_num; i++) {
        int index1 = dis1(gen1);
        int num1 = freq_sort1[index1].second;
        if (find(selected1.begin(), selected1.end(), num1) == selected1.end()) {
            selected1.push_back(num1);
        }
    }

    random_device rd2;
    mt19937 gen2(rd2());
    uniform_int_distribution<> dis2(21, freq_sort1.size()-1);  // 从后15个元素中选择

    for (int i = 0; i < front_cold_num; i++) {
        int index1 = dis2(gen2);
        int num1 = freq_sort1[index1].second;
        if (find(selected1.begin(), selected1.end(), num1) == selected1.end()) {
            selected1.push_back(num1);
        }
    }

    //后2区
    vector<int> selected2;
    random_device rd3;
    mt19937 gen3(rd3());
    uniform_int_distribution<> dis3(0, 6);  // 从前6个元素中选择

    for (int i = 0; i < back_hot_num; i++)  {
        int index2 = dis3(gen3);
        int num2 = freq_sort2[index2].second;
        if (find(selected2.begin(), selected2.end(), num2) == selected2.end()) {
            selected2.push_back(num2);
        }
    }

    random_device rd4;
    mt19937 gen4(rd4());
    uniform_int_distribution<> dis4(6, freq_sort2.size()-1);  // 从后6个元素中选择

    for (int i = 0; i < back_cold_num; i++) {
        int index2 = dis4(gen4);
        int num2 = freq_sort2[index2].second;
        if (find(selected2.begin(), selected2.end(), num2) == selected2.end()) {
            selected2.push_back(num2);
        }
    }

    // 打印结果
    cout << "Selected numbers from freq_sort1: ";
    for (int num : selected1) {
        cout << num << " ";
    }
    cout << endl;

    cout << "Selected numbers from freq_sort2: ";
    for (int num : selected2) {
        cout << num << " ";
    }
    cout << endl;
}

//release
//冷门前5区：15选5 + 后2区：6选2并打印结果
void generateOutputcold(const vector<pair<int, int>>& freq_sort1, const vector<pair<int, int>>& freq_sort2) {
    // 随机选取freq_sort1中的5个元素
    vector<int> selected1;
    random_device rd1;
    mt19937 gen1(rd1());
    uniform_int_distribution<> dis1(21, freq_sort1.size()-1);  // 从后15个元素中选择

    while (selected1.size() < 5) {
        int index1 = dis1(gen1);
        int num1 = freq_sort1[index1].second;
        if (find(selected1.begin(), selected1.end(), num1) == selected1.end()) {
            selected1.push_back(num1);
        }
    }

    // 随机选取freq_sort2中的2个元素
    vector<int> selected2;
    random_device rd2;
    mt19937 gen2(rd2());
    uniform_int_distribution<> dis2(6, freq_sort2.size()-1);  // 从后5个元素中选择

    while (selected2.size() < 2) {
        int index2 = dis2(gen2);
        int num2 = freq_sort2[index2].second;
        if (find(selected2.begin(), selected2.end(), num2) == selected2.end()) {
            selected2.push_back(num2);
        }
    }

    // 打印结果
    cout << "Selected numbers from freq_sort1: ";
    for (int num : selected1) {
        cout << num << " ";
    }
    cout << endl;

    cout << "Selected numbers from freq_sort2: ";
    for (int num : selected2) {
        cout << num << " ";
    }
    cout << endl;
}

//release
//热门前5区：15选5 + 后2区：6选2并打印结果
void generateOutputhot(const vector<pair<int, int>>& freq_sort1, const vector<pair<int, int>>& freq_sort2) {

    // 随机选取freq_sort1中的5个元素
    vector<int> selected1;
    random_device rd1;
    mt19937 gen1(rd1());
    uniform_int_distribution<> dis1(0, 14);  // 从前15个元素中选择

    while (selected1.size() < 5) {
        int index1 = dis1(gen1);
        int num1 = freq_sort1[index1].second;
        if (find(selected1.begin(), selected1.end(), num1) == selected1.end()) {
            selected1.push_back(num1);
        }
    }

    // 随机选取freq_sort2中的2个元素
    vector<int> selected2;
    random_device rd2;
    mt19937 gen2(rd2());
    uniform_int_distribution<> dis2(0, 6);  // 从前5个元素中选择

    while (selected2.size() < 2) {
        int index2 = dis2(gen2);
        int num2 = freq_sort2[index2].second;
        if (find(selected2.begin(), selected2.end(), num2) == selected2.end()) {
            selected2.push_back(num2);
        }
    }

    // 打印结果
    cout << "Selected numbers from freq_sort1: ";
    for (int num : selected1) {
        cout << num << " ";
    }
    cout << endl;

    cout << "Selected numbers from freq_sort2: ";
    for (int num : selected2) {
        cout << num << " ";
    }
    cout << endl;
}


//release
//1.从文件中读取数据，分为5+2对各个数值出现次数进行统计
//2.对结果排序并打印
void calculateFrequency() {

    ifstream inFile("array.txt");
    if (!inFile) {
        cerr << "Failed to open file" << endl;
        exit(1);
    }

    int arr[ARRAY_LIMIT][ARRAY_SIZE];

    // 从文件中读取数据
    for (int i = 0; i < ARRAY_LIMIT; i++) {
        for (int j = 0; j < ARRAY_SIZE; j++) {
            inFile >> arr[i][j];
        }
    }

    // 统计每个数字出现的频率
    for (int i = 0; i < ARRAY_LIMIT; i++) {
        for (int j = 0; j < ARRAY_SIZE; j++) {
            if (j < 5) {
                freq1[arr[i][j]]++;
            } else {
                freq2[arr[i][j]]++;
            }
        }
    }

    // 对freq1和freq2排序
    for (int i = 1; i < 36; i++) {
        freq1_sort.emplace_back(freq1[i], i);
    }
    for (int i = 1; i < 13; i++) {
        freq2_sort.emplace_back(freq2[i], i);
    }

    sort(freq1_sort.rbegin(), freq1_sort.rend());
    sort(freq2_sort.rbegin(), freq2_sort.rend());
    // 打印结果
    cout << "five statistics:" << endl;
    for (auto p : freq1_sort) {
        cout << p.second << ":" << p.first << endl;
    }

    cout << "last two statistics:" << endl;
    for (auto p : freq2_sort) {
        cout << p.second << ":" << p.first << endl;
    }
}

//release:
//1.从文件中读取数据，不足100就填充，保持最多100组数据
//2.更新入参到文件中，并保存
void saveArray(int arr[]) {
    // 读取原有的数组数据
    ifstream fin("array.txt");
    vector<string> lines;
    string line;
    if (fin.is_open()) {
        while (getline(fin, line)) {
            lines.push_back(line);
        }
        fin.close();
    }

    // 去掉最开头的一组数据，保证整个文件一直只有100组数据
    if (lines.size() >= ARRAY_LIMIT) {
        lines.erase(lines.begin());
    }

    // 将新数组添加到文件末尾
    ofstream fout("array.txt", ios::app);
    if (fout.is_open()) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            fout << arr[i] << " ";
        }
        fout << endl;
        fout.close();
    }
}

int main() {

    srand(time(0));  // 使用当前时间作为种子
    HappyCash* test = new HappyCash();
    if (test == nullptr) {
        cout << "new HappyCash failed!" << endl;
        return 0;
    }


    char input;
    cout << "input cmd: " << endl;
    cout << "   1->random generation" << endl;
    cout << "   q->quit" << endl;
    while (true) {
        cin >> input;
        if (input == '1') {
            test->ClearResult();
            test->QueryRandomResultFive(3, 1, 1);
            test->QueryRandomResultTwo(1, 0, 1);
            test->PrintResult();
        }else if (input == 'q') {
            delete test;
            break;
        }
    }
    return 0;
}
//
// Created by yi.ji on 2023/4/23.
//
#include "happycash.h"
#include <iostream>
#include <fstream>
#include <algorithm>
#include "string.h"

using namespace std;

HappyCash::HappyCash() {
    cout << "HappyCash constructor" << endl;

    //1.open file "array.txt"
    ifstream inFile("../array.txt");    //note:Path is relative to the executable file
    if (!inFile) {
        cerr << "Failed to open file" << endl;
        exit(1);
    }

    //2.read data from file and store in 'source'
    source = new int[ARRAY_LIMIT * ARRAY_SIZE];
    for (int i = 0; i < ARRAY_LIMIT; i++) {
        for (int j = 0; j < ARRAY_SIZE; j++) {
            inFile >> source[i * ARRAY_SIZE + j];
        }
    }

    //DEBUG: print source
//    for (int i = 0; i < ARRAY_LIMIT; i++) {
//        cout << "source[" << i << "]: ";
//        for (int j = 0; j < ARRAY_SIZE; j++) {
//            cout << source[i * ARRAY_SIZE + j] << " ";
//        }
//        cout << endl;
//    }

    //3.calculate frequency
    freq_five = new int[FIVE_RANGE + 1];
    freq_two = new int[TWO_RANGE + 1];
    memset(freq_five, 0, sizeof(int) * (FIVE_RANGE + 1));
    memset(freq_two, 0, sizeof(int) * (TWO_RANGE + 1));

    for (int i = 0; i < ARRAY_LIMIT; i++) {
        for (int j = 0; j < ARRAY_SIZE; j++) {
            if (j < 5) {
                freq_five[source[i * ARRAY_SIZE + j]]++;
            } else {
                freq_two[source[i * ARRAY_SIZE + j]]++;
            }
        }
    }

    //4.sort
    for (int i = 1; i < FIVE_RANGE + 1; i++) {
        freq_five_sort.emplace_back(freq_five[i], i);
    }
    for (int i = 1; i < TWO_RANGE + 1; i++) {
        freq_two_sort.emplace_back(freq_two[i], i);
    }
    sort(freq_five_sort.rbegin(), freq_five_sort.rend());
    sort(freq_two_sort.rbegin(), freq_two_sort.rend());

    //5.init data
    InitFiveData();
    InitTwoData();
}

void HappyCash::QueryRandomResultFive(int head_choose, int mid_choose, int tail_choose) {

    //1.将five_selected_head中的元素随机打乱
    random_shuffle(five_selected_head, five_selected_head + five_length[0]);
    while (result_five.size() < head_choose) {
        int index = rand() % five_length[0];
        if (result_five.find(five_selected_head[index]) == result_five.end()) {  // 判断随机选取的数字是否已经在集合中出现过
            result_five.insert(five_selected_head[index]);  // 将对应的数插入集合
        }
    }

    //2.将five_selected_mid中的元素随机打乱
    random_shuffle(five_selected_mid, five_selected_mid + five_length[1]);
    while (result_five.size() < head_choose + mid_choose) {
        int index = rand() % five_length[1];
        if (result_five.find(five_selected_mid[index]) == result_five.end()) {  // 判断随机选取的数字是否已经在集合中出现过
            result_five.insert(five_selected_mid[index]);  // 将对应的数插入集合
        }
    }

    //3.将five_selected_tail中的元素随机打乱
    random_shuffle(five_selected_tail, five_selected_tail + five_length[2]);
    while (result_five.size() < head_choose + mid_choose + tail_choose) {
        int index = rand() % five_length[2];
        if (result_five.find(five_selected_tail[index]) == result_five.end()) {  // 判断随机选取的数字是否已经在集合中出现过
            result_five.insert(five_selected_tail[index]);  // 将对应的数插入集合
        }
    }

    //4.print result
//    cout << "five result: " << endl;
//    for (auto p : result_five) {
//        cout << p << " ";
//    }
//    cout << endl;
}

void HappyCash::QueryRandomResultTwo(int head_choose, int mid_choose, int tail_choose) {
    //1.将two_selected_head中的元素随机打乱
    random_shuffle(two_selected_head, two_selected_head + two_length[0]);
    while (result_two.size() < head_choose) {
        int index = rand() % two_length[0];
        if (result_two.find(two_selected_head[index]) == result_two.end()) {  // 判断随机选取的数字是否已经在集合中出现过
            result_two.insert(two_selected_head[index]);  // 将对应的数插入集合
        }
    }

    //2.将two_selected_mid中的元素随机打乱
    random_shuffle(two_selected_mid, two_selected_mid + two_length[1]);
    while (result_two.size() < head_choose + mid_choose) {
        int index = rand() % two_length[1];
        if (result_two.find(two_selected_mid[index]) == result_two.end()) {  // 判断随机选取的数字是否已经在集合中出现过
            result_two.insert(two_selected_mid[index]);  // 将对应的数插入集合
        }
    }

    //2.将two_selected_tail中的元素随机打乱
    random_shuffle(two_selected_tail, two_selected_tail + two_length[2]);
    while (result_two.size() < head_choose + mid_choose + tail_choose) {
        int index = rand() % two_length[2];
        if (result_two.find(two_selected_tail[index]) == result_two.end()) {  // 判断随机选取的数字是否已经在集合中出现过
            result_two.insert(two_selected_tail[index]);  // 将对应的数插入集合
        }
    }

    //4.print result
//    cout << "two result: " << endl;
//    for (auto p : result_two) {
//        cout << p << " ";
//    }
//    cout << endl;
}

void HappyCash::ClearResult() {
    result_five.clear();
    result_two.clear();
}

void HappyCash::PrintResult() {
    cout << "result: " << endl;
    for (auto p : result_five) {
        cout << p << " ";
    }
    for (auto p : result_two) {
        cout << p << " ";
    }
    cout << endl;
}

void HappyCash::CheckOneArray(int *array) {
    cout << "five head values: ";
    for (int i = 0; i < 5; i++) {
        //如果array[i]在five_selected_head中出现过，则打印出来
        for (int j = 0; j < five_length[0]; j++) {
            if (five_selected_head[j] == array[i]){
                cout << array[i] << " ";
                break;
            }
        }
    }
    cout << endl;

    cout << "five mid values: ";
    for (int i = 0; i < 5; i++) {
        //如果array[i]在five_selected_mid中出现过，则打印出来
        for (int j = 0; j < five_length[1]; j++) {
            if (five_selected_mid[j] == array[i]){
                cout << array[i] << " ";
                break;
            }
        }
    }
    cout << endl;

    cout << "five tail values: ";
    for (int i = 0; i < 5; i++) {
        //如果array[i]在five_selected_tail中出现过，则打印出来
        for (int j = 0; j < five_length[2]; j++) {
            if (five_selected_tail[j] == array[i]){
                cout << array[i] << " ";
                break;
            }
        }
    }
    cout << endl;

    cout << "two head values: ";
    for (int i = 5; i < 7; i++) {
        //如果array[i]在two_selected_head中出现过，则打印出来
        for (int j = 0; j < two_length[0]; j++) {
            if (two_selected_head[j] == array[i]){
                cout << array[i] << " ";
                break;
            }
        }
    }
    cout << endl;

    cout << "two mid values: ";
    for (int i = 5; i < 7; i++) {
        //如果array[i]在two_selected_mid中出现过，则打印出来
        for (int j = 0; j < two_length[1]; j++) {
            if (two_selected_mid[j] == array[i]){
                cout << array[i] << " ";
                break;
            }
        }
    }
    cout << endl;

    cout << "two tail values: ";
    for (int i = 5; i < 7; i++) {
        //如果array[i]在two_selected_tail中出现过，则打印出来
        for (int j = 0; j < two_length[2]; j++) {
            if (two_selected_tail[j] == array[i]){
                cout << array[i] << " ";
                break;
            }
        }
    }
    cout << endl;
}

void HappyCash::PrintSortArray() {
//    cout << "freq_five_sort: " << endl;
////    for (auto p : freq_five_sort) {
////        cout << p.second << ":" << p.first << endl;
////    }
////    cout << endl;
////    cout << "freq_two_sort: " << endl;
////    for (auto p : freq_two_sort) {
////        cout << p.second << ":" << p.first << endl;
////    }
////    cout << endl;
    cout << "freq_five_sort: " << endl;
    cout << "head: ";
    for (int i = 0; i < 12; i++) {
        cout << " ";
        cout << freq_five_sort[i].second << "(" << freq_five_sort[i].first << ")";
    }
    cout << endl;
    cout << "mid:  ";
    for (int i = 12; i < 24; i++) {
        cout << " ";
        cout << freq_five_sort[i].second << "(" << freq_five_sort[i].first << ")";
    }
    cout << endl;
    cout << "tail: ";
    for (int i = 24; i < 35; i++) {
        cout << " ";
        cout << freq_five_sort[i].second << "(" << freq_five_sort[i].first << ")";
    }
    cout << endl;
    cout << endl;
    cout << "freq_two_sort: " << endl;
    cout << "head: ";
    for (int i = 0; i < 4; i++) {
        cout << " ";
        cout << freq_two_sort[i].second << "(" << freq_two_sort[i].first << ")";
    }
    cout << endl;
    cout << "mid:  ";
    for (int i = 4; i < 8; i++) {
        cout << " ";
        cout << freq_two_sort[i].second << "(" << freq_two_sort[i].first << ")";
    }
    cout << endl;
    cout << "tail: ";
    for (int i = 8; i < 12; i++) {
        cout << " ";
        cout << freq_two_sort[i].second << "(" << freq_two_sort[i].first << ")";
    }
    cout << endl;
}

void HappyCash::InitFiveData() {
    //1.cal head, mid, tail sum
    int sum_head = 0;
    int sum_mid = 0;
    int sum_tail = 0;

    for (int i = 0; i < 12; i++) {
        sum_head += freq_five_sort[i].first;
    }
    for (int i = 12; i < 24; i++) {
        sum_mid += freq_five_sort[i].first;
    }
    for (int i = 24; i < 35; i++) {
        sum_tail += freq_five_sort[i].first;
    }

    cout << "total: " << sum_head << " + " << sum_mid << " + " << sum_tail << " = " << sum_head + sum_mid + sum_tail
         << endl;

    //2.allocate memory
    five_selected_head = new int[sum_head];
    five_selected_mid = new int[sum_mid];
    five_selected_tail = new int[sum_tail];

    //3.fill five_selected_head
    int index = 0;
    for (int i = 0; i < 12; i++) {
        for (int j = 0; j < freq_five_sort[i].first; j++) {
            five_selected_head[index++] = freq_five_sort[i].second;
        }
    }
    //DEBUG: print five_selected_head
//    cout << "five_selected_head: " << endl;
//    for (int i = 0; i < sum_head; i++) {
//        cout << five_selected_head[i] << " ";
//    }
//    cout << endl;

    //4.fill five_selected_mid
    index = 0;
    for (int i = 12; i < 24; i++) {
        for (int j = 0; j < freq_five_sort[i].first; j++) {
            five_selected_mid[index++] = freq_five_sort[i].second;
        }
    }
    //DEBUG: print five_selected_mid
//    cout << "five_selected_mid: " << endl;
//    for (int i = 0; i < sum_mid; i++) {
//        cout << five_selected_mid[i] << " ";
//    }
//    cout << endl;

    //5.fill five_selected_tail
    index = 0;
    for (int i = 24; i < 35; i++) {
        for (int j = 0; j < freq_five_sort[i].first; j++) {
            five_selected_tail[index++] = freq_five_sort[i].second;
        }
    }

    //DEBUG: print five_selected_tail
//    cout << "five_selected_tail: " << endl;
//    for (int i = 0; i < sum_tail; i++) {
//        cout << five_selected_tail[i] << " ";
//    }
//    cout << endl;

    //6.update length
    memset(five_length, 0, sizeof(five_length));
    five_length[0] = sum_head;
    five_length[1] = sum_mid;
    five_length[2] = sum_tail;
}

void HappyCash::InitTwoData() {
    //1.cal head, mid, tail sum
    int sum_head = 0;
    int sum_mid = 0;
    int sum_tail = 0;

    for (int i = 0; i < 4; i++) {
        sum_head += freq_two_sort[i].first;
    }
    for (int i = 4; i < 8; i++) {
        sum_mid += freq_two_sort[i].first;
    }
    for (int i = 8; i < 12; i++) {
        sum_tail += freq_two_sort[i].first;
    }

    cout << "total: " << sum_head << " + " << sum_mid << " + " << sum_tail << " = " << sum_head + sum_mid + sum_tail <<endl;

    //2.allocate memory
    two_selected_head = new int[sum_head];
    two_selected_mid = new int[sum_mid];
    two_selected_tail = new int[sum_tail];

    //3.fill two_selected_head
    int index = 0;
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < freq_two_sort[i].first; j++) {
            two_selected_head[index++] = freq_two_sort[i].second;
        }
    }
    //DEBUG: print two_selected_head
//    cout << "two_selected_head: " << endl;
//    for (int i = 0; i < sum_head; i++) {
//        cout << two_selected_head[i] << " ";
//    }
//    cout << endl;

    //4.fill two_selected_mid
    index = 0;
    for (int i = 4; i < 8; i++) {
        for (int j = 0; j < freq_two_sort[i].first; j++) {
            two_selected_mid[index++] = freq_two_sort[i].second;
        }
    }
    //DEBUG: print two_selected_mid
//    cout << "two_selected_mid: " << endl;
//    for (int i = 0; i < sum_mid; i++) {
//        cout << two_selected_mid[i] << " ";
//    }
//    cout << endl;

    //5.fill two_selected_tail
    index = 0;
    for (int i = 8; i < 12; i++) {
        for (int j = 0; j < freq_two_sort[i].first; j++) {
            two_selected_tail[index++] = freq_two_sort[i].second;
        }
    }

    //DEBUG: print two_selected_tail
//    cout << "two_selected_tail: " << endl;
//    for (int i = 0; i < sum_tail; i++) {
//        cout << two_selected_tail[i] << " ";
//    }
//    cout << endl;
    //6.update length
    memset(two_length, 0, sizeof(two_length));
    two_length[0] = sum_head;
    two_length[1] = sum_mid;
    two_length[2] = sum_tail;
}

HappyCash::~HappyCash() {
    cout << "HappyCash destructor" << endl;
    delete[] source;
    delete[] freq_five;
    delete[] freq_two;
    delete[] five_selected_head;
    delete[] five_selected_mid;
    delete[] five_selected_tail;
    delete[] two_selected_head;
    delete[] two_selected_mid;
    delete[] two_selected_tail;
}
